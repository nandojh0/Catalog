/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.Catalog.service;

import com.example.Catalog.models.Order;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author nando
 */
public interface OrderService {
     List<Order> getAllOrders();
    Optional<Order> getOrderById(Long orderId);
    Order createOrder(Order orderDetails);
    Optional<Order> updateOrder(Long orderId, Order orderDetails);
    void deleteOrder(Long orderId);
}
