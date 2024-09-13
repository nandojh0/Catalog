/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.service.Impl;

import com.example.Catalog.models.Category;
import com.example.Catalog.models.CategoryDto;
import com.example.Catalog.models.eceptions.CustomException;
import com.example.Catalog.repository.CategoryRepository;
import com.example.Catalog.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 *
 * @author nando
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryDto categoryDto) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDto.getName());
        if (existingCategory.isPresent()) {
            throw new CustomException("Ya existe una categoría con el nombre: " + categoryDto.getName(), "409");
        }

        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();
        // Guardar la categoría en la base de datos
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Error al guardar la categoría. Verifique los datos proporcionados.", "409");
        } catch (Exception e) {
            throw new CustomException("Error inesperado al guardar la categoría.", "500");
        }
    }

    @Override
    public void updateCategory(Long id, CategoryDto categoryDto) {
        // Buscar la categoría existente
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException("Categoría no encontrada con id: " + id, "404"));

        try {

            // Verificar si el nombre de la categoría ya está en uso
            Optional<Category> existingCategoryWithName = categoryRepository.findByName(categoryDto.getName());
            if (existingCategoryWithName.isPresent() && !existingCategoryWithName.get().getId().equals(id)) {
                throw new CustomException("Ya existe una categoría con el nombre: " + categoryDto.getName(), "409");
            }

            // Actualizar los atributos de la categoría existente
            existingCategory.setName(categoryDto.getName());
            // Asignar otros campos si es necesario
            // existingCategory.setOtherField(categoryDto.getOtherField());

            categoryRepository.save(existingCategory);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Error al actualizar la categoría. Verifique los datos proporcionados.", "409");
        } catch (Exception e) {
            throw new CustomException("Error inesperado al actualizar la categoría.", "500");
        }
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
