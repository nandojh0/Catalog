/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.controller;

/**
 *
 * @author nando
 */
import com.example.Catalog.models.Category;
import com.example.Catalog.models.CategoryDto;
import com.example.Catalog.models.Response;
import com.example.Catalog.models.eceptions.CustomException;
import com.example.Catalog.service.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping("/all")
    public ResponseEntity<Response<List<Category>>> getAllCategories() {
        try {
            List<Category> Categories = categoryService.getAllCategories();
            Response<List<Category>> response = Response.<List<Category>>builder()
                    .message("Categorias recuperados exitosamente.")
                    .code("200")
                    .data(Categories)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<List<Category>> response = Response.<List<Category>>builder()
                    .message("Error al recuperar productos: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoryDto DTO que contiene la información de la categoría.
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/create")
    public ResponseEntity<Response<String>> createCategory(@RequestBody CategoryDto categoryDto) {
        // Validar el DTO
        Optional<String> validationError = categoryDto.validate();
        if (validationError.isPresent()) {
            Response<String> response = Response.<String>builder()
                    .message(validationError.get())
                    .code("400")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        try {

            // Llamar al servicio para guardar la categoría
            categoryService.createCategory(categoryDto);
            Response<String> response = Response.<String>builder()
                    .message("Categoría creada exitosamente.")
                    .code("201")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            Response<String> response = Response.<String>builder()
                    .message(e.getMessage())
                    .code(e.getErrorCode())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al crear la categoria")
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
    }

    /**
     * Actualizar una categoría existente
     *
     * @param categoryDto
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/update")
    public ResponseEntity<Response<String>> updateCategory(@RequestBody CategoryDto categoryDto) {
        // Validar el DTO
        Optional<String> validationError = categoryDto.validate();
        if (validationError.isPresent()) {
            Response<String> response = Response.<String>builder()
                    .message(validationError.get())
                    .code("400")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // Actualizar la categoría
            categoryService.updateCategory(categoryDto.getId(), categoryDto);
            Response<String> response = Response.<String>builder()
                    .message("Categoría actualizada exitosamente.")
                    .code("201")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            Response<String> response = Response.<String>builder()
                    .message(e.getMessage())
                    .code(e.getErrorCode())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al crear la categoria")
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar una categoría por ID
     *
     * @param categoryDto
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/find")
    public ResponseEntity<Response<Category>> getCategoryById(@RequestBody CategoryDto categoryDto) {
         // Validar el DTO
        Optional<String> validationError = categoryDto.validate();
        if (validationError.isPresent()) {
            Response<Category> response = Response.<Category>builder()
                    .message(validationError.get())
                    .code("400")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        Optional<Category> category = categoryService.findCategoryById(categoryDto.getId());
        if (category.isPresent()) {
            // Si la categoría existe, devolvemos la categoría
            Response<Category> response = Response.<Category>builder()
                            .message("Categoría encontrada.")
                            .code("200")
                            .data(category.get())
                            .build();
             return ResponseEntity.ok(response);
        } else {
            // Si no se encuentra la categoría, enviamos un error detallado
             Response<Category> response = Response.<Category>builder()
                    .message("Categoría no encontrada con el ID: " + categoryDto.getId())
                    .code("404")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
