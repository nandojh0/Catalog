/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.controller;

import com.example.Catalog.models.Order;
import com.example.Catalog.models.OrderDto;
import com.example.Catalog.models.Response;
import com.example.Catalog.models.eceptions.CustomException;
import com.example.Catalog.service.Impl.OrderMapper;
import com.example.Catalog.service.OrderService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nando
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/all")
    public ResponseEntity<Response<List<OrderDto>>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            List<OrderDto> orderDtos = orders.stream()
                    .map(order -> OrderMapper.toDto((Order) order)) // Casting explícito
                    .collect(Collectors.toList());

            Response<List<OrderDto>> response = Response.<List<OrderDto>>builder()
                    .message("Pedidos recuperados exitosamente.")
                    .code("200")
                    .data(orderDtos)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<List<OrderDto>> response = Response.<List<OrderDto>>builder()
                    .message("Error al recuperar los pedidos: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/find/json")
    public ResponseEntity<Response<OrderDto>> getOrderById(@RequestBody OrderDto orderDto) {
        try {
            Optional<String> validationError = orderDto.validate();
            if (validationError.isPresent()) {
                Response<OrderDto> response = Response.<OrderDto>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Order> order = orderService.getOrderById(orderDto.getId());
            if (order.isPresent()) {
                OrderDto dto = OrderMapper.toDto(order.get());
                Response<OrderDto> response = Response.<OrderDto>builder()
                        .message("Pedido encontrado.")
                        .code("200")
                        .data(dto)
                        .build();
                return ResponseEntity.ok(response);
            } else {
                Response<OrderDto> response = Response.<OrderDto>builder()
                        .message("Pedido no encontrado.")
                        .code("404")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Response<OrderDto> response = Response.<OrderDto>builder()
                    .message("Error al buscar el pedido: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/create/json")
    public ResponseEntity<Response<String>> createOrder(@RequestBody OrderDto orderDto) {
        try {
            // Validar campos
            Optional<String> validationError = orderDto.validateAll();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            // Convertir DTO a entidad
            Order order = OrderMapper.toEntity(orderDto);

            // Crear el pedido
            Order createdOrder = orderService.createOrder(order);

            // Respuesta exitosa
            Response<String> response = Response.<String>builder()
                    .message("Pedido creado exitosamente.")
                    .code("201")
                    .data("Pedido creado con ID: " + createdOrder.getId())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (CustomException e) {
            // Respuesta personalizada para errores de lógica de negocio
            Response<String> response = Response.<String>builder()
                    .message("Error al crear el pedido: " + e.getMessage())
                    .code(e.getErrorCode()) // Código de error adecuado
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Manejar errores
            Response<String> response = Response.<String>builder()
                    .message("Error al crear el pedido: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update/json")
    public ResponseEntity<Response<String>> updateOrder(@RequestBody OrderDto orderDto) {
        try {
            Optional<String> validationError = orderDto.validatePut();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            // Convertir DTO a entidad
            Order order = OrderMapper.toEntity(orderDto);

            // Actualizar el pedido
            Optional<Order> updatedOrder = orderService.updateOrder(orderDto.getId(), order);
            if (updatedOrder.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message("Pedido actualizado exitosamente.")
                        .code("200")
                        .data("Pedido actualizado con ID: " + updatedOrder.get().getId())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                Response<String> response = Response.<String>builder()
                        .message("Pedido no encontrado.")
                        .code("404")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (CustomException e) {
            // Respuesta personalizada para errores de negocio como producto inexistente o stock insuficiente
            Response<String> response = Response.<String>builder()
                    .message("Error al actualizar el pedido: " + e.getMessage())
                    .code(e.getErrorCode())
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al actualizar el pedido: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/json")
    public ResponseEntity<Response<String>> deleteOrder(@RequestBody OrderDto orderDto) {
        try {
            Optional<String> validationError = orderDto.validate();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }
            

            orderService.deleteOrder(orderDto.getId());
            Response<String> response = Response.<String>builder()
                    .message("Pedido eliminado exitosamente.")
                    .code("204")
                    .build();
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            // Respuesta personalizada para errores de negocio como producto inexistente o stock insuficiente
            Response<String> response = Response.<String>builder()
                    .message("Error al eliminar el pedido: " + e.getMessage())
                    .code(e.getErrorCode())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al eliminar el pedido: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
