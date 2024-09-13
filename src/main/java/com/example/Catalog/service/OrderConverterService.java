/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.Catalog.service;

import com.example.Catalog.models.Order;
import com.example.Catalog.models.OrderDto;

/**
 *
 * @author nando
 */
public interface OrderConverterService {
    public Order convertToOrder(OrderDto orderDto);
    
}
