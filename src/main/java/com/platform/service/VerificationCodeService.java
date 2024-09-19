package com.platform.service;

import com.platform.domain.VerificationType;
import com.platform.model.User;
import com.platform.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerification(VerificationCode verificationCode);
}
