package com.platform.service;

import com.platform.model.PaymentDetails;
import com.platform.model.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolder, String ifsc, String bankName, User user);

    public PaymentDetails getUsersPayentDetails(User user);

}
