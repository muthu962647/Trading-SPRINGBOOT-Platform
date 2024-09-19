package com.platform.service;

import com.platform.domain.OrderStatus;
import com.platform.domain.OrderType;
import com.platform.model.*;
import com.platform.repositry.OrderItemRepositry;
import com.platform.repositry.OrderRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepositry orderRepositry;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepositry orderItemRepositry;

    @Autowired
    private AssetService assetService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {

        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepositry.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepositry.findById(orderId)
                .orElseThrow(()-> new Exception("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepositry.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice){
        OrderItem orderItem= new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        orderItemRepositry.save(orderItem);
        return orderItem;
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("quantiy should be  greater than 0");
        }

        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);

        walletService.payOrderPayment(order,user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepositry.save(order);

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());

        if(oldAsset == null){
            assetService.createAsset(user,orderItem.getCoin(),orderItem.getQuantity());
        }else{
            assetService.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;

    }


    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("quantiy should be  greater than 0");
        }

        double sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(),coin.getId());

        if(assetToSell != null) {

            OrderItem orderItem = createOrderItem(coin, quantity, 0, sellPrice);
            Order order = createOrder(user, orderItem, OrderType.SELL);

            double buyPrice = assetToSell.getBuyPrice();

            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {

                order.setStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepositry.save(order);

                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }
            throw new Exception("Insufficient quantity to sell");
        }
        throw new Exception("asset not found");
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin,quantity,user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin,quantity,user);
        }
        throw new Exception("invalid Order Type");
    }
}
