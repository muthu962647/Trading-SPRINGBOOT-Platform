package com.platform.service;

import com.platform.model.User;
import com.platform.model.Withdrawl;

import java.util.List;

public interface WithdrwalService {

    Withdrawl requestWithdrwal(Long amount, User user);

    Withdrawl proceedWithdrwal(Long withdrawlId, boolean accept) throws Exception;

    List<Withdrawl> getUserWithdrawlHistory(User user);

    List<Withdrawl> getAllWithdrawlRequest();

}
