package com.platform.service;

import com.platform.model.PaymentDetails;
import com.platform.model.User;
import com.platform.repositry.PaymentDetailsRepositry;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    @Autowired
    private PaymentDetailsRepositry paymentDetailsRepositry;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolder, String ifsc, String bankName, User user) {

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountName(accountNumber);
        paymentDetails.setAccountHolder(accountHolder);
        paymentDetails.setIFSC(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        return paymentDetailsRepositry.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUsersPayentDetails(User user) {
        return paymentDetailsRepositry.findByUserId(user.getId());
    }
}
