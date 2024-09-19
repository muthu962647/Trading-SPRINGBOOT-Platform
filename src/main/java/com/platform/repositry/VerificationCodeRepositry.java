package com.platform.repositry;

import com.platform.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepositry extends JpaRepository<VerificationCode,Long> {

    public VerificationCode findByUserId(Long userId);
}
