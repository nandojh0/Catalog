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
public class UpdateProductFields {
    
    public static Product updateProductFields(Product existingProduct, ProductDto productDto) {
    if (productDto.getProduct().getName() != null) {
        existingProduct.setName(productDto.getProduct().getName());
    }
    if (productDto.getProduct().getDescription() != null) {
        existingProduct.setDescription(productDto.getProduct().getDescription());
    }
    if (productDto.getProduct().getPrice() != null) {
        existingProduct.setPrice(productDto.getProduct().getPrice());
    }
    if (productDto.getProduct().getCategory() != null) {
        existingProduct.setCategory(productDto.getProduct().getCategory());
    }
    if (productDto.getProduct().getStock() != null) {
        existingProduct.setStock(productDto.getProduct().getStock());
    }
    return existingProduct;
}

    
}
