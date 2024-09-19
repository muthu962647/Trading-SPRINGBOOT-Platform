package com.platform.service;

import com.platform.model.TwoFactorOtp;
import com.platform.model.User;
import com.platform.repositry.TwoFactoryOtpRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{

    @Autowired
    private TwoFactoryOtpRepositry twoFactoryOtpRepositry;

    @Override
    public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();

        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);

        return  twoFactoryOtpRepositry.save(twoFactorOtp);

    }

    @Override
    public TwoFactorOtp findByUser(Long user) {
        return twoFactoryOtpRepositry.findByUserId(user);
    }

    @Override
    public TwoFactorOtp findByOtp(String otp) {
        return null;
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> opt = twoFactoryOtpRepositry.findById(id);
        return opt.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
         twoFactoryOtpRepositry.delete(twoFactorOtp);
    }
}
