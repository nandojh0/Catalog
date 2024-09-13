/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.service.Impl;

import com.example.Catalog.models.Product;
import com.example.Catalog.models.ProductDto;

/**
 *
 * @author nando
 */
public class ProductMapper {

    // Convierte un Product a ProductDto
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDto.builder()
                .id(product.getId())
                .product(product) // O puedes mapear los campos individualmente si es necesario
                .build();
    }

    // Convierte un ProductDto a Product
    public static Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getProduct().getName())
                .description(productDto.getProduct().getDescription())
                .price(productDto.getProduct().getPrice())
                .category(productDto.getProduct().getCategory())
                .stock(productDto.getProduct().getStock())
                .build();
    }
}