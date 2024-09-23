package com.platform.service;

import com.platform.domain.PaymentMethod;
import com.platform.model.PaymentOrder;
import com.platform.model.User;
import com.platform.request.PaymentResponse;
import com.razorpay.RazorpayException;

public interface PaymentService {

        PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

        PaymentOrder getPaymentOrderById(Long id) throws Exception;

        Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;

        PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException;

        PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId);


}
