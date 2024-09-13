/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.models;

/**
 *
 * @author nando
 */
import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;

    /**
     * Valida los campos del DTO y devuelve un mensaje que indica qué campo es inválido.
     * @return Mensaje de error o null si todos los campos son válidos.
     */
    public Optional<String> validate() {
        if (name == null || name.isEmpty()) {
            return Optional.of("El campo 'name' no puede ser nulo o vacío.");
        }
        return Optional.empty(); // Todos los campos son válidos
    }
}
