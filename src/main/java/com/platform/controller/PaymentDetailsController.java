package com.platform.controller;

import com.platform.model.PaymentDetails;
import com.platform.model.User;
import com.platform.service.PaymentDetailsService;
import com.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsRequest,
            @RequestHeader("Aauthorization") String jwt
    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolder(),
                paymentDetailsRequest.getIFSC(),
                paymentDetailsRequest.getBankName(),
                user
        );

        return ResponseEntity.ok(paymentDetails);
    }
}
