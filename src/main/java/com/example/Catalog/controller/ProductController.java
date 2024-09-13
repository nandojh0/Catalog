/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.controller;

import com.example.Catalog.models.Product;
import com.example.Catalog.models.ProductDto;
import com.example.Catalog.models.Response;
import com.example.Catalog.service.Impl.UpdateProductFields;
import com.example.Catalog.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nando
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/all")
    public ResponseEntity<Response<List<Product>>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            Response<List<Product>> response = Response.<List<Product>>builder()
                    .message("Productos recuperados exitosamente.")
                    .code("200")
                    .data(products)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<List<Product>> response = Response.<List<Product>>builder()
                    .message("Error al recuperar productos: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/find/json")
    public ResponseEntity<Response<Product>> getProductById(@RequestBody ProductDto productDto) {
        try {
            Optional<String> validationError = productDto.validate();
            if (validationError.isPresent()) {
                Response<Product> response = Response.<Product>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Product> product = productService.getProductById(productDto.getId());
            if (product.isPresent()) {
                Response<Product> response = Response.<Product>builder()
                        .message("Producto encontrado.")
                        .code("200")
                        .data(product.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                Response<Product> response = Response.<Product>builder()
                        .message("Producto no encontrado.")
                        .code("404")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Response<Product> response = Response.<Product>builder()
                    .message("Error al recuperar el producto: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/create/json")
    public ResponseEntity<Response<String>> createProduct(@RequestBody ProductDto productDto) {
        try {
            Optional<String> validationError = productDto.validateAll();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            Product product = productDto.getProduct();
            productService.createProduct(product);
//            Product createdProduct = productService.createProduct(product);
            Response<String> response = Response.<String>builder()
                    .message("Producto creado exitosamente.")
                    .code("201")
                    //                    .data("Producto creado con ID: " + createdProduct.getId())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al crear el producto: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update/json")
    public ResponseEntity<Response<String>> updateProduct(@RequestBody ProductDto productDto) {
        try {
            Optional<String> validationError = productDto.validatePut();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }

            // Obtener el producto existente desde la base de datos
            Optional<Product> existingProductOpt = productService.getProductById(productDto.getId());
            if (!existingProductOpt.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message("Producto no encontrado.")
                        .code("404")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Product existingProduct = existingProductOpt.get();

            // Actualizar solo los campos que no sean nulos en el DTO
            UpdateProductFields.updateProductFields(existingProduct, productDto);

            // Guardar el producto actualizado
            Product updatedProduct = productService.updateProduct(existingProduct);

            // Respuesta exitosa
            Response<String> response = Response.<String>builder()
                    .message("Producto actualizado exitosamente.")
                    .code("200")
                    .data("Producto actualizado con ID: " + updatedProduct.getId())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al actualizar el producto: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/json")
    public ResponseEntity<Response<String>> deleteProduct(@RequestBody ProductDto productDto) {
        try {
            Optional<String> validationError = productDto.validate();
            if (validationError.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message(validationError.get())
                        .code("400")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }
            // Verificar si el producto existe antes de intentar eliminarlo
            Optional<Product> existingProductOpt = productService.getProductById(productDto.getId());
            if (!existingProductOpt.isPresent()) {
                Response<String> response = Response.<String>builder()
                        .message("Producto no encontrado.")
                        .code("404")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            productService.deleteProduct(productDto.getId());
            Response<String> response = Response.<String>builder()
                    .message("Producto eliminado exitosamente.")
                    .code("204")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<String> response = Response.<String>builder()
                    .message("Error al eliminar el producto: " + e.getMessage())
                    .code("500")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
