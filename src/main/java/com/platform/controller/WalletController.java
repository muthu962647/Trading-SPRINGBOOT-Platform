package com.platform.controller;

import com.platform.model.Order;
import com.platform.model.User;
import com.platform.model.Wallet;
import com.platform.request.WalletTransaction;
import com.platform.service.OrderService;
import com.platform.service.UserService;
import com.platform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/wallet/{walletId}/pay")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @RequestBody WalletTransaction req
            ) throws Exception{

        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(req.getId());
        Wallet wallet = walletService.walletToWalletTransfer(senderUser,receiverWallet,req.getAmount());

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) throws Exception{

        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order,user);

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }
}
