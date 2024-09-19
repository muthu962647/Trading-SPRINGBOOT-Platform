package com.platform.controller;

import com.platform.request.ForgotPasswordTokenRequest;
import com.platform.domain.VerificationType;
import com.platform.model.ForgotPasswordToken;
import com.platform.model.User;
import com.platform.model.VerificationCode;
import com.platform.request.ResetPasswordRequest;
import com.platform.response.ApiResponse;
import com.platform.response.AuthResponse;
import com.platform.service.EmailService;
import com.platform.service.ForgotPasswordService;
import com.platform.service.UserService;
import com.platform.service.VerificationCodeService;
import com.platform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificaionType}/{otp}")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode == null){
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }

        return new ResponseEntity<String>("Verification Otp Send Succesfully", HttpStatus.OK);

    }


    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp
    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),sendTo,user);

            verificationCodeService.deleteVerification(verificationCode);

            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }
        throw new Exception("Wrong OTP");
    }


    @PostMapping("/api/users/reset-password/sent-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestHeader("Authorization") String jwt,
            @RequestBody ForgotPasswordTokenRequest req) throws Exception
             {

        User user = userService.findUserProfileByJwt(jwt);
        String otp = OtpUtils.generateOtp();
        UUID uuid  = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token == null){
            token = forgotPasswordService.createToken(user,id,otp,req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset OTP Send Successfully");

        return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);

    }

    @PatchMapping("/auth/users/reset-password/verify_otp")
    public ResponseEntity<ApiResponse> resetPasswordOtp(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @PathVariable String otp
    ) throws Exception {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());

        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password update Successfully");
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong Otp");
    }
}
