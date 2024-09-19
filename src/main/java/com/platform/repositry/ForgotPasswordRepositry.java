package com.platform.repositry;

import com.platform.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepositry extends JpaRepository<ForgotPasswordToken,String> {

    ForgotPasswordToken findByUserId(Long userId);

}
