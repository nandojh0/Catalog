/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author nando
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private Double total;
    private List<OrderItemDto> items;


    /**
     * Valida el campo 'userId' en el DTO.
     * @return Mensaje de error en caso de que 'userId' sea nulo o inválido, o Optional.empty() si es válido.
     */
    public Optional<String> validate() {
        if (id == null || id <= 0 ) {
            return Optional.of("El campo 'id' no puede ser nulo o menor que 1.");
        }
        return Optional.empty(); // El campo es válido
    }

    /**
     * Valida todos los campos del DTO y devuelve un mensaje que indica qué campo es inválido.
     * @return Mensaje de error o Optional.empty() si todos los campos son válidos.
     */
    public Optional<String> validateAll() {
        
        if (orderDate == null) {
            return Optional.of("El campo 'orderDate' no puede ser nulo.");
        }

        if (status == null || status.isEmpty()) {
            return Optional.of("El campo 'status' no puede ser nulo o vacío.");
        }

        if (items == null || items.isEmpty()) {
            return Optional.of("El campo 'items' no puede ser nulo o vacío.");
        }

        for (OrderItemDto item : items) {
            Optional<String> itemValidation = item.validate();
            if (itemValidation.isPresent()) {
                return itemValidation;
            }
        }

        return Optional.empty(); // Todos los campos son válidos
    }
    
    /**
     * Valida el campo 'userId' en el DTO.
     * @return  Mensaje de error en caso de que 'userId' sea nulo o inválido, o Optional.empty() si es válido.
     */
    public Optional<String> validatePut() {
        if (id == null || id <= 0 ) {
            return Optional.of("El campo 'id' no puede ser nulo o menor que 1.");
        }
        if (orderDate == null) {
            return Optional.of("El campo 'orderDate' no puede ser nulo.");
        }

        if (status == null || status.isEmpty()) {
            return Optional.of("El campo 'status' no puede ser nulo o vacío.");
        }

        if (items == null || items.isEmpty()) {
            return Optional.of("El campo 'items' no puede ser nulo o vacío.");
        }

        for (OrderItemDto item : items) {
            Optional<String> itemValidation = item.validate();
            if (itemValidation.isPresent()) {
                return itemValidation;
            }
        }

        return Optional.empty(); // Todos los campos son válidos
    }
    
}

