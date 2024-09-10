package com.platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private USER_ROLE role = USER_ROLE.ROLE_ADMIN;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

}
