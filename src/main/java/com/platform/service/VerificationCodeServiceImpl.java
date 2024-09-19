package com.platform.service;

import com.platform.domain.VerificationType;
import com.platform.model.User;
import com.platform.model.VerificationCode;
import com.platform.repositry.VerificationCodeRepositry;
import com.platform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    private VerificationCodeRepositry verificationCodeRepositry;

    @Autowired
    private UserService userService;


    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);

        return verificationCodeRepositry.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {

        Optional<VerificationCode> verificationCode = verificationCodeRepositry.findById(id);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("Verification code nt Found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepositry.findByUserId(userId);
    }

    @Override
    public void deleteVerification(VerificationCode verificationCode) {
        verificationCodeRepositry.delete(verificationCode);
    }
}
