package com.platform.service;

import com.platform.domain.VerificationType;
import com.platform.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserProfileByEmail(String email) throws Exception;
    public User findUserById(Long userId) throws Exception;
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);
    User updatePassword(User user, String newPassword);


}
