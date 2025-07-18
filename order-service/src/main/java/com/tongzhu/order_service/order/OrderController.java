package com.tongzhu.order_service.order;

import com.fasterxml.jackson.core.JsonProcessingException;


import com.tongzhu.common_dto.ItemsDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final RedisTemplate<String, String> redisTemplate;


    public OrderController(OrderService orderService, RedisTemplate<String, String> redisTemplate) {
        this.orderService = orderService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{id}")
    ResponseEntity<Order> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping()
    ResponseEntity<String> postOrder(@RequestBody ItemsDTO itemsDTO) throws JsonProcessingException {

        return ResponseEntity.ok(orderService.sendRequest(itemsDTO));


    }

    @GetMapping("/status")
    ResponseEntity<String> getStatus(@RequestParam String uuid) {
        String status = redisTemplate.opsForValue().get("order:status:" + uuid);
        return status == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(status);

    }








}
