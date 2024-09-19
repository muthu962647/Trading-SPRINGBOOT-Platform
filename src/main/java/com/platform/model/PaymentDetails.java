package com.platform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PaymentDetails {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountNumber;

    private String accountName;

    private String AccountHolder;

    private String IFSC;

    private String bankName;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

}
