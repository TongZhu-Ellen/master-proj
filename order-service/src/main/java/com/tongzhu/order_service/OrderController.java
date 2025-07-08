package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    ResponseEntity<Order> postOrder(@RequestBody ItemsDTO itemsDTO) throws JsonProcessingException {

        Order order = orderService.postOrder(itemsDTO);

        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(order);


    }


}
