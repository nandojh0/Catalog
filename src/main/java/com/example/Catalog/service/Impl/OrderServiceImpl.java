/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.service.Impl;

import com.example.Catalog.models.Order;
import com.example.Catalog.models.OrderItem;
import com.example.Catalog.models.Product;
import com.example.Catalog.models.eceptions.CustomException;
import com.example.Catalog.repository.OrderRepository;
import com.example.Catalog.repository.ProductRepository;
import com.example.Catalog.service.OrderService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nando
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        double totalPrice = 0.0;

        // Verificar cada producto en la lista de items de la orden
        for (OrderItem item : order.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());

            if (productOpt.isEmpty()) {
                throw new CustomException("El producto con ID " + item.getProductId() + " no existe.", "409");
            }

            Product product = productOpt.get();

            // Verificar si hay suficiente stock
            if (product.getStock() < item.getQuantity()) {
                throw new CustomException("No hay suficiente stock para el producto " + product.getName(), "409");
            }

            // Ajustar el precio del producto
            item.setPrice(product.getPrice());

            // Restar el stock del producto
            product.setStock(product.getStock() - item.getQuantity());

            // Actualizar el precio total
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Establecer el precio total de la orden
        order.setTotal(totalPrice);

        // Guardar la orden
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> updateOrder(Long orderId, Order orderDetails) {
        // Buscar si la orden existe
        Optional<Order> existingOrderOpt = orderRepository.findById(orderId);
        if (existingOrderOpt.isEmpty()) {
            return Optional.empty();  // Si no existe, retornamos vacío
        }

        Order existingOrder = existingOrderOpt.get();
        double totalPrice = 0.0;

        // Crear un mapa para almacenar la cantidad total de productos en la nueva orden
        Map<Long, Integer> newItemsMap = new HashMap<>();
        for (OrderItem item : orderDetails.getItems()) {
            newItemsMap.put(item.getProductId(), item.getQuantity());
        }

        // Crear un mapa para almacenar la cantidad total de productos en la orden existente
        Map<Long, Integer> existingItemsMap = new HashMap<>();
        for (OrderItem item : existingOrder.getItems()) {
            existingItemsMap.put(item.getProductId(), item.getQuantity());
        }

        // Verificar el stock para los productos que se están agregando o actualizando
        for (OrderItem item : orderDetails.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());

            if (productOpt.isEmpty()) {
                throw new CustomException("El producto con ID " + item.getProductId() + " no existe.", "409");
            }

            Product product = productOpt.get();
            int newQuantity = item.getQuantity();
            int existingQuantity = existingItemsMap.getOrDefault(item.getProductId(), 0);

            // Verificar stock: si la cantidad ha aumentado, necesitamos ajustar el stock
            if (newQuantity > existingQuantity) {
                int quantityToCheck = newQuantity - existingQuantity;
                if (product.getStock() < quantityToCheck) {
                    throw new CustomException("No hay suficiente stock para el producto " + product.getName(), "409");
                }
            }

            // Ajustar el precio del producto en el item
            item.setPrice(product.getPrice());

            // Calcular el precio total de los items
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Ajustar el stock de los productos eliminados o actualizados
        for (Map.Entry<Long, Integer> entry : existingItemsMap.entrySet()) {
            Long productId = entry.getKey();
            int oldQuantity = entry.getValue();
            int newQuantity = newItemsMap.getOrDefault(productId, 0);

            if (newQuantity < oldQuantity) {
                Optional<Product> productOpt = productRepository.findById(productId);
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    product.setStock(product.getStock() + (oldQuantity - newQuantity));
                    productRepository.save(product);
                }
            }
        }

        // Actualizar el stock de los productos agregados en la nueva orden
        for (OrderItem item : orderDetails.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int newQuantity = item.getQuantity();
                int existingQuantity = existingItemsMap.getOrDefault(item.getProductId(), 0);

                if (newQuantity > existingQuantity) {
                    int quantityToDeduct = newQuantity - existingQuantity;
                    product.setStock(product.getStock() - quantityToDeduct);
                    productRepository.save(product);
                }
            }
        }

        // Actualizar el precio total de la orden
        existingOrder.setTotal(totalPrice);

        // Actualizar el resto de los detalles de la orden
        existingOrder.setStatus(orderDetails.getStatus());
        existingOrder.setOrderDate(orderDetails.getOrderDate());
        existingOrder.setItems(orderDetails.getItems());

        // Guardar la orden actualizada en la base de datos
        orderRepository.save(existingOrder);

        return Optional.of(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        // Buscar la orden para verificar su estado
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new CustomException("La orden con ID " + id + " no existe.", "409");
        }

        Order order = orderOpt.get();

        // Verificar si la orden está en estado CANCELLED
        if (!"CANCELLED".equals(order.getStatus())) {
            throw new CustomException("La orden con ID " + id + " no puede ser eliminada porque no está en estado CANCELLED.", "409");
        }

        // Recuperar el stock de los ítems de la orden
        for (OrderItem item : order.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isEmpty()) {
                throw new CustomException("El producto con ID " + item.getProductId() + " no existe.", "409");
            }

            Product product = productOpt.get();
            // Aumentar el stock del producto
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        // Eliminar la orden
        orderRepository.deleteById(id);

    }
}
