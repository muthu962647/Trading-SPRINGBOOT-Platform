package com.platform.repositry;

import com.platform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemRepositry extends JpaRepository<OrderItem,Long> {

}
