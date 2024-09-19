package com.platform.service;

import com.platform.domain.VerificationType;
import com.platform.model.ForgotPasswordToken;
import com.platform.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user,
                                    String id,
                                    String otp,
                                    VerificationType verificationType,
                                    String sendTo);
    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
