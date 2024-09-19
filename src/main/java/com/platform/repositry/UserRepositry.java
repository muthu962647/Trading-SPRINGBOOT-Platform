package com.platform.repositry;

import com.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositry extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
