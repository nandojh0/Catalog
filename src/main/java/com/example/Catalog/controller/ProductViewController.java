/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.controller;

import com.example.Catalog.models.Category;
import com.example.Catalog.models.Product;
import com.example.Catalog.models.ProductDto;
import com.example.Catalog.service.CategoryService;
import com.example.Catalog.service.Impl.ProductMapper;
import com.example.Catalog.service.Impl.UpdateProductFields;
import com.example.Catalog.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author nando
 */
@Controller
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String listProducts(Model model) {
        try {
            
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            return "list-product";
        } catch (Exception e) {
            model.addAttribute("error", "Error al recuperar productos: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                model.addAttribute("product", product.get());
                return "view-product";
            } else {
                model.addAttribute("error", "Producto no encontrado.");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al recuperar el producto: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Category> Categories = categoryService.getAllCategories();
        ProductDto productDto = ProductDto.builder()
                .product(new Product())
                .build();
        System.out.println("ProductDto: " + productDto);
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", Categories);
        return "form-product";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            Optional<String> validationError = productDto.validateAll();
            if (validationError.isPresent()) {
                redirectAttributes.addFlashAttribute("error", validationError.get());
                return "redirect:/products/create";
            }

            Product product = productDto.getProduct();
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("message", "Producto creado exitosamente.");
            return "redirect:/products/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el producto: " + e.getMessage());
            return "redirect:/products/create";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                ProductDto productDto = ProductMapper.toDto(product.get());
                model.addAttribute("productDto", productDto);
                return "form-product";
            } else {
                model.addAttribute("error", "Producto no encontrado.");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al recuperar el producto: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            Optional<String> validationError = productDto.validate();
            if (validationError.isPresent()) {
                redirectAttributes.addFlashAttribute("error", validationError.get());
                return "redirect:/products/update/" + productDto.getId();
            }

            Optional<Product> existingProductOpt = productService.getProductById(productDto.getId());
            if (!existingProductOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
                return "redirect:/products/update/" + productDto.getId();
            }

            Product existingProduct = existingProductOpt.get();
            UpdateProductFields.updateProductFields(existingProduct, productDto);
            Product updatedProduct = productService.updateProduct(existingProduct);
            redirectAttributes.addFlashAttribute("message", "Producto actualizado exitosamente.");
            return "redirect:/products/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            return "redirect:/products/update/" + productDto.getId();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Producto eliminado exitosamente.");
            return "redirect:/products/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
            return "redirect:/products/list";
        }
    }
}
