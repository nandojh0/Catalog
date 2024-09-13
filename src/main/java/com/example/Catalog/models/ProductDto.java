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
public class ProductDto {

    private Long id;
    private Product product;

    /**
     * Valida los campos del DTO y devuelve un mensaje que indica qué campo es
     * inválido.
     *
     * @return Mensaje de error o null si todos los campos son válidos.
     */
    public Optional<String> validate() {
        if (id == null || id <= 0 ) {
            return Optional.of("El campo 'id' no puede ser nulo o menor que 1.");
        }
        return Optional.empty(); // Todos los campos son válidos
    }

    /**
     * Valida los campos del DTO y devuelve un mensaje que indica qué campo es
     * inválido.
     *
     * @return Mensaje de error o null si todos los campos son válidos.
     */
    public Optional<String> validateAll() {
        if (product == null) {
            return Optional.of("El campo 'product' no puede ser nulo.");
        }

        if (product.getName() == null || product.getName().isEmpty()) {
            return Optional.of("El campo 'name' en 'product' no puede ser nulo o vacío.");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            return Optional.of("El campo 'price' en 'product' debe ser mayor que 0.");
        }

        if (product.getCategory() == null || product.getCategory().getId() == null) {
            return Optional.of("El campo 'category' en 'product' no puede ser nulo o vacío.");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            return Optional.of("El campo 'stock' en 'product' no puede ser negativo.");
        }

        return Optional.empty(); // Todos los campos son válidos
    }

    public Optional<String> validatePut() {
         if (id == null) {
            return Optional.of("El campo 'id' no puede ser nulo.");
        }
        if (product == null) {
            return Optional.of("El campo 'product' no puede ser nulo.");
        }
        if (product.getName() != null) {
            if (product.getName().isEmpty()) {
                return Optional.of("El campo 'name' en 'product' no puede  vacío.");
            }
        }
        if (product.getPrice() != null) {
            if (product.getPrice() <= 0) {
                return Optional.of("El campo 'price' en 'product' debe ser mayor que 0.");
            }
        }
        if (product.getCategory() != null) {
            if (product.getCategory().getId() == null) {
            return Optional.of("El campo 'idcategory' en 'product' no puede ser nulo");
        }
        }
        

        if (product.getStock() != null) {
            if (product.getStock() < 0) {
                return Optional.of("El campo 'stock' en 'product' no puede ser negativo.");
            }
        }

        return Optional.empty(); // Todos los campos son válidos
    }

}
