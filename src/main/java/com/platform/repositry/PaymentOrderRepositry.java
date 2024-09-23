package com.platform.repositry;

import com.platform.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepositry extends JpaRepository<PaymentOrder, Long> {
}
