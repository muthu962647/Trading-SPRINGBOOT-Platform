package com.platform.repositry;

import com.platform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepositry extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}
