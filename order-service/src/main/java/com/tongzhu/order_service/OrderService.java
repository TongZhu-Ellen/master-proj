package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongzhu.order_service.dto.ItemsDTO;
import com.tongzhu.order_service.dto.RequestDTO;
import com.tongzhu.order_service.exception.OrderNotFoundException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());
    }




    public String postOrder(ItemsDTO itemsDTO) {
        String requestId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend("request_queue",
                new RequestDTO(requestId, itemsDTO));
        return requestId;

    }


}
