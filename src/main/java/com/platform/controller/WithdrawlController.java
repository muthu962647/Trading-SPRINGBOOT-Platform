package com.platform.controller;

import com.platform.model.User;
import com.platform.model.Wallet;
import com.platform.model.WalletTransaction;
import com.platform.model.Withdrawl;
import com.platform.service.UserService;
import com.platform.service.WalletService;
import com.platform.service.WithdrwalService;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawl")
public class WithdrawlController {

    @Autowired
    private WithdrwalService withdrwalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    @PostMapping("/api/withdrawl/{amount}")
    public ResponseEntity<?> withdrawlRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);
        Withdrawl withdrawl = withdrwalService.requestWithdrwal(amount,user);
        walletService.addBalance(wallet,-withdrawl.getAmount());

//        WalletTransaction walletTransaction = Wa

        return new ResponseEntity<Withdrawl>(withdrawl, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawl/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawl(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Withdrawl withdrawl = withdrwalService.proceedWithdrwal(id, accept);

        Wallet userWallet = walletService.getUserWallet(user);

        if(!accept){
            walletService.addBalance(userWallet, withdrawl.getAmount());
        }

        return new ResponseEntity<>(withdrawl,HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawl")
    public ResponseEntity<List<Withdrawl>> getAllWithdrawlRequest(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawl> withdrawl = withdrwalService.getAllWithdrawlRequest();

        return new ResponseEntity<>(withdrawl,HttpStatus.OK);

    }

}
