package com.platform.repositry;

import com.platform.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepositry extends JpaRepository<PaymentDetails,Long> {
    PaymentDetails findByUserId(Long userId);
}
