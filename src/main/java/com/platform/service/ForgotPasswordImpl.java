package com.platform.service;

import com.platform.domain.VerificationType;
import com.platform.model.ForgotPasswordToken;
import com.platform.model.User;
import com.platform.repositry.ForgotPasswordRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordImpl implements  ForgotPasswordService{
    @Autowired
    private ForgotPasswordRepositry forgotPasswordRepositry;

    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepositry.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepositry.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
       return forgotPasswordRepositry.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepositry.delete(token);
    }
}
