package com.platform.service;

import com.platform.domain.PaymentMethod;
import com.platform.domain.PaymentOrderStatus;
import com.platform.model.PaymentOrder;
import com.platform.model.User;
import com.platform.repositry.PaymentOrderRepositry;
import com.platform.request.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentOrderRepositry paymentOrderRepositry;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setPaymentMethod(paymentMethod);

        return paymentOrderRepositry.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepositry.findById(id).orElseThrow(() ->  new Exception("payment order not found"));
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }

                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepositry.save(paymentOrder);

                return false;
            }

            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepositry.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException {

        Long Amount = amount*100;

        try{
            RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);

            JSONObject paymentLinkRequest = new JSONObject();

            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getName());

            JSONObject notify = new JSONObject();
            notify.put("email",user.getEmail());

            paymentLinkRequest.put("notify", notify);
            paymentLinkRequest.put("reminder_enable",true);

            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = payment.get("id");
            res.setPayment_url(paymentLinkUrl);

            return res;

        } catch (RazorpayException e) {
            System.out.println("Error creating exception "+ e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(amount * 100)  // Convert amount to cents (or smallest unit)
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Top up wallet")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
                )
                .build();

        Session session = Session.create(params);

        System.out.println("Session... " + session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());

        return res;
    }
}
