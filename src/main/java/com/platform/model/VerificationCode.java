package com.platform.model;

import com.platform.domain.VerificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String mobile;

    private VerificationType verificationType;
}
