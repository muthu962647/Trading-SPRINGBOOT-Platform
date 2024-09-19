package com.platform.repositry;

import com.platform.model.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactoryOtpRepositry extends JpaRepository<TwoFactorOtp, String> {
    TwoFactorOtp findByUserId(Long UserId);
}
