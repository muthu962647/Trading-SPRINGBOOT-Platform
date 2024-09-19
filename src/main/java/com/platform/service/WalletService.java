package com.platform.service;

import com.platform.model.Order;
import com.platform.model.User;
import com.platform.model.Wallet;

public interface WalletService {

    Wallet getUserWallet (User user);

    Wallet addBalance(Wallet wallet,Long money);

    Wallet findWalletById(Long Id) throws Exception;

    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;


}
