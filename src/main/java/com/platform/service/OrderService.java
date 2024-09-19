package com.platform.service;

import com.platform.domain.OrderType;
import com.platform.model.Coin;
import com.platform.model.Order;
import com.platform.model.OrderItem;
import com.platform.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long OrderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}