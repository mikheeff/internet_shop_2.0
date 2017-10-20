package com.internetshop.repository.api;

import com.internetshop.entities.*;

import java.util.List;
import java.util.Set;

public interface OrderRepository {
    List<DeliveryMethodEntity> getAllDeliveryMethods();
    List<PaymentTypeEntity> getAllPaymentTypes();
    List<StatusEntity> getAllStatuses();
    StatusEntity getStatusById(int id);
    int getIdPaymentTypeByName(String name);
    int getIdDeliveryMethodByName(String name);
    int addOrder(OrderEntity orderEntity, Set<CartItemEntity> cartItemEntitySet);
    List<OrderEntity> getAllOrdersByClientId(int id);
    List<OrderEntity> getAllOrders();
    OrderEntity getOrderById(int id);
    List<CartItemEntity> getAllCartItemsFromOrderByOrderId(int id);
}