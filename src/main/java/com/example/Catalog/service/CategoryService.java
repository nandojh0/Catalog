/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.Catalog.service;

import com.example.Catalog.models.Category;
import com.example.Catalog.models.CategoryDto;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author nando
 */
public interface CategoryService {
    public void createCategory(CategoryDto categoryDto);
    public void updateCategory(Long id, CategoryDto categoryDto);
    public Optional<Category> findCategoryById(Long id);
    public List<Category> getAllCategories();
}
