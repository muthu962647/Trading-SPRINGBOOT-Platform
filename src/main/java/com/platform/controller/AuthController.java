package com.platform.controller;

import com.platform.config.JwtProvider;
import com.platform.model.TwoFactorOtp;
import com.platform.model.User;
import com.platform.repositry.UserRepositry;
import com.platform.response.AuthResponse;
import com.platform.service.CustomUserDetailsService;
import com.platform.service.EmailService;
import com.platform.service.TwoFactorOtpService;
import com.platform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist = userRepositry.findByEmail(user.getEmail());


        if(isEmailExist != null){
            throw new Exception("Email is Already Exists with another Account");
        }

        User savedUser = userRepositry.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();

        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register Success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
        
        String userName = user.getName();
        
        String password = user.getPassword();

        Authentication auth = authenticate( userName, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepositry.findByEmail(userName);

        if(user.getTwoFactorAuth().isEnabled()){
             AuthResponse res = new AuthResponse();
             res.setMessage("TwoFactorAuth is Enabled");
             res.setTwoFactorAuthEnabled(true);
             String otp = OtpUtils.generateOtp();

            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());

            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);

            emailService.sendVerificationOtpEmail(userName,otp);

            res.setSession(newTwoFactorOtp.getId());

            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

        }

        AuthResponse res = new AuthResponse();

        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        if(userDetails == null){
            throw new BadCredentialsException("invalid userName");
        }
        if(password.equals(userDetails.getPassword())){

        }else{
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/{otp}")
    public ResponseEntity<AuthResponse> verifySigningInOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {

        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp,otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOtp.getJwt());
            res.setJwt(twoFactorOtp.getJwt());
            return new ResponseEntity<>(res,HttpStatus.OK);
        }

        throw new Exception("invalid otp");
    }


}
