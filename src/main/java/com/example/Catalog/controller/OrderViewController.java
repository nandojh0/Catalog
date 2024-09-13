/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Catalog.controller;

import com.example.Catalog.models.Order;
import com.example.Catalog.models.OrderDto;
import com.example.Catalog.models.eceptions.CustomException;
import com.example.Catalog.service.Impl.OrderMapper;
import com.example.Catalog.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
@RequestMapping("/orders")
public class OrderViewController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public String listOrders(Model model) {
        List<OrderDto> orders = orderService.getAllOrders()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("orders", orders);
        return "order-list"; // Nombre de la vista de Thymeleaf para listar pedidos
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        
        OrderDto orderDto = OrderDto.builder()
                .build();
        model.addAttribute("orderDto", orderDto);
        return "order-form"; // Formulario para crear un pedido
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("orderDto") OrderDto orderDto, RedirectAttributes redirectAttributes) {
        try {
            Order order = OrderMapper.toEntity(orderDto);
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("message", "Pedido creado exitosamente");
            return "redirect:/orders/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el pedido: " + e.getMessage());
            return "redirect:/orders/create";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            OrderDto orderDto = OrderMapper.toDto(order.get());
            model.addAttribute("orderDto", orderDto);
            try {
                // Convertir los ítems del pedido a JSON y agregarlo como un campo oculto
                model.addAttribute("itemsJson", new ObjectMapper().writeValueAsString(orderDto.getItems()));
            } catch (JsonProcessingException ex) {
               model.addAttribute("error", "Error al recuperar los productos: " + ex.getMessage());
            return "error";
            }
            return "order-form"; // Reutilizamos la misma vista para crear y actualizar
        } else {
            return "redirect:/orders/list";
        }
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute("orderDto") OrderDto orderDto, RedirectAttributes redirectAttributes) {
        try {
            Order order = OrderMapper.toEntity(orderDto);
            orderService.updateOrder(orderDto.getId(), order);
            redirectAttributes.addFlashAttribute("message", "Pedido actualizado exitosamente");
            return "redirect:/orders/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el pedido: " + e.getMessage());
            return "redirect:/orders/update/" + orderDto.getId();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("message", "Pedido eliminado exitosamente");
            return "redirect:/orders/list";
        
            } catch (CustomException ex) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el pedido: " + ex.getMessage());
            return "redirect:/orders/list";
        }
         catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el pedido: " + e.getMessage());
            return "redirect:/orders/list";
        }
    }
}
