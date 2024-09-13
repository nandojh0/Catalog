/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.Catalog.repository;

import com.example.Catalog.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nando
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}