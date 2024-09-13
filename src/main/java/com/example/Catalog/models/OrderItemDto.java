/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.models;

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

public class OrderItemDto {

    private Long productId;
    private Integer quantity;
    private Double price;
    private Double priceSubTotal;

    /**
     * Valida los campos del DTO Item.
     * @return Mensaje de error en caso de que algún campo sea nulo o inválido, o Optional.empty() si es válido.
     */
    public Optional<String> validate() {
        if (productId == null || productId <= 0) {
            return Optional.of("El campo 'productId' debe ser mayor que 0.");
        }

        if (quantity == null || quantity <= 0) {
            return Optional.of("El campo 'quantity' debe ser mayor que 0.");
        }
        return Optional.empty(); // Todos los campos son válidos
    }
}
