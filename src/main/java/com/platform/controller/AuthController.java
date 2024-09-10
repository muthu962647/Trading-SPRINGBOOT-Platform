package com.platform.controller;

import com.platform.model.User;
import com.platform.repositry.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepositry userRepositry;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody User user){

        User savedUser = userRepositry.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


}
