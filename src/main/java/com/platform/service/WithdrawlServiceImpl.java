package com.platform.service;

import com.platform.domain.WithdrawlStatus;
import com.platform.model.User;
import com.platform.model.Withdrawl;
import com.platform.repositry.WithdrawlRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawlServiceImpl implements WithdrwalService{

    @Autowired
    private WithdrawlRepositry withdrawlRepositry;

    @Override
    public Withdrawl requestWithdrwal(Long amount, User user) {

        Withdrawl withdrawl = new Withdrawl();
        withdrawl.setAmount(amount);
        withdrawl.setUser(user);
        withdrawl.setStatus(WithdrawlStatus.PENDING);

        return withdrawlRepositry.save(withdrawl);
    }

    @Override
    public Withdrawl proceedWithdrwal(Long withdrawlId, boolean accept) throws Exception {

        Optional<Withdrawl> withdrawl = withdrawlRepositry.findById(withdrawlId);
        if(withdrawl.isEmpty()){
            throw new Exception("Withdrawl not found");
        }

        Withdrawl withdrawl1 = withdrawl.get();

        withdrawl1.setLocalDateTime(LocalDateTime.now());

        if(accept){
            withdrawl1.setStatus(WithdrawlStatus.SUCCESS);
        }else {
            withdrawl1.setStatus(WithdrawlStatus.SUCCESS);
        }
        return withdrawlRepositry.save(withdrawl1);
    }

    @Override
    public List<Withdrawl> getUserWithdrawlHistory(User user) {
        return withdrawlRepositry.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawl> getAllWithdrawlRequest() {
        return withdrawlRepositry.findAll();
    }
}

