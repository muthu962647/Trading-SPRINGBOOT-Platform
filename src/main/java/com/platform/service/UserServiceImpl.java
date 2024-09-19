package com.platform.service;

import com.platform.config.JwtProvider;
import com.platform.domain.VerificationType;
import com.platform.model.TwoFactorAuth;
import com.platform.model.User;
import com.platform.repositry.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositry userRepositry;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepositry.findByEmail(email);

        if(user == null){
            throw new Exception("User not Found");
        }
        return user;
    }

    @Override
    public User findUserProfileByEmail(String email) throws Exception {
        User user = userRepositry.findByEmail(email);

        if(user == null){
            throw new Exception("User not Found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepositry.findById(userId);
        if (user.isEmpty()){
            throw new Exception("User not Found");
        }
        return user.get();
    }


    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepositry.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepositry.save(user);
    }
}
