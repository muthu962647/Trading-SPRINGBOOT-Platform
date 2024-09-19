package com.platform.request;

import com.platform.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
