/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.service.Impl;

/**
 *
 * @author nando
 */
import com.example.Catalog.models.Order;
import com.example.Catalog.models.OrderDto;
import com.example.Catalog.models.OrderItem;
import com.example.Catalog.models.OrderItemDto;
import java.util.stream.Collectors;

public class OrderMapper {

    /**
     * Convierte un Order a un OrderDto.
     * @param order Entidad Order a convertir.
     * @return OrderDto convertido.
     */
    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .total(order.getTotal())
                .items(order.getItems() != null 
                    ? order.getItems().stream()
                        .map(OrderMapper::toDto) // Convertir cada OrderItem a OrderItemDto
                        .collect(Collectors.toList())
                    : null)
                .build();
    }

    /**
     * Convierte un OrderDto a un Order.
     * @param orderDto DTO OrderDto a convertir.
     * @return Order convertido.
     */
    public static Order toEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        
        return Order.builder()
                .userId(orderDto.getUserId())
                .orderDate(orderDto.getOrderDate())
                .status(orderDto.getStatus())
                .items(orderDto.getItems() != null
                    ? orderDto.getItems().stream()
                        .map(OrderMapper::toEntity) // Convertir cada OrderItemDto a OrderItem
                        .collect(Collectors.toSet())
                    : null)
                .build();
    }

    /**
     * Convierte un OrderItem a un OrderItemDto.
     * @param orderItem Entidad OrderItem a convertir.
     * @return OrderItemDto convertido.
     */
    public static OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        return OrderItemDto.builder()
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .priceSubTotal(orderItem.getPriceSubTotal())
                .build();
    }

    /**
     * Convierte un OrderItemDto a un OrderItem.
     * @param orderItemDto DTO OrderItemDto a convertir.
     * @return OrderItem convertido.
     */
    public static OrderItem toEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }
        
        return OrderItem.builder()
                .productId(orderItemDto.getProductId())
                .quantity(orderItemDto.getQuantity())
                .price(0.0) // Asigna un valor por defecto
                .build();
    }
}
