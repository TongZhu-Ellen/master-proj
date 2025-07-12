package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.tongzhu.order_service.dto.ItemsDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    ResponseEntity<Order> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping()
    ResponseEntity<String> postOrder(@RequestBody ItemsDTO itemsDTO) throws JsonProcessingException {
        String requestId = orderService.postOrder(itemsDTO);
        return ResponseEntity.ok(requestId);



    }









}
