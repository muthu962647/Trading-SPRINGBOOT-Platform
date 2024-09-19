package com.platform.service;

import com.platform.domain.OrderType;
import com.platform.model.Order;
import com.platform.model.User;
import com.platform.model.Wallet;
import com.platform.repositry.WalletRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepositry walletRepositry;

    @Override
    public Wallet getUserWallet(User user) {

        Wallet wallet = walletRepositry.findByUserId(user.getId());
        if(wallet == null){
            wallet = new Wallet();
            wallet.setUser(user);
        }

        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepositry.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long Id) throws Exception {
        Optional<Wallet> wallet = walletRepositry.findById(Id);
        if (wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
            throw new Exception("Unsufficient Balance");
        }

        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepositry.save(senderWallet);

        BigDecimal receieverBalance = receiverWallet
                .getBalance()
                .add(BigDecimal.valueOf(amount));

        receiverWallet.setBalance(receieverBalance);

        walletRepositry.save(receiverWallet);

        return null;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);

        if(order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if(newBalance.compareTo(order.getPrice()) < 0){
                throw new Exception("Insufficent funds for this transaction");
            }
            wallet.setBalance(newBalance);
            walletRepositry.save(wallet);
        }else {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }

        walletRepositry.save(wallet);

        return wallet;
    }
}
