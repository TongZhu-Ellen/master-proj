package com.tongzhu.order_service.order;

import com.fasterxml.jackson.core.JsonProcessingException;


import com.tongzhu.order_service.OrderStatus;
import com.tongzhu.order_service.dto.ItemsDTO;
import com.tongzhu.order_service.dto.RequestDTO;

import com.tongzhu.order_service.exception.OrderNotFoundException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tongzhu.order_service.RabbitUtil.*;


@Service
public class OrderService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;




    public OrderService(RedisTemplate<String, String> redisTemplate,
                        OrderRepository orderRepository,
                        RabbitTemplate rabbitTemplate) {
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());
    }





    // risk here:
    // it might happen that redis works but rabbit doesn't;
    public String sendRequest(ItemsDTO itemsDTO) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();


        redisTemplate.opsForValue().set("order:status:" + uuid,
                OrderStatus.PENDING.toString());

        rabbitTemplate.convertAndSend("request_queue",
                convertToString(new RequestDTO(uuid, itemsDTO, OrderStatus.PENDING)));

        return uuid;

    }

    @RabbitListener(queues = "response_queue")
    public void getResponse(String string) throws JsonProcessingException {
        RequestDTO requestDTO = readFromString(string);
        String uuid = requestDTO.uuid();
        ItemsDTO itemsDTO = requestDTO.itemsDTO();
        OrderStatus orderStatus = requestDTO.orderStatus();

        if (OrderStatus.SUCCEED.equals(orderStatus)) {
            orderRepository.save(new Order(itemsDTO));
        } else if (OrderStatus.PENDING.equals(orderStatus)) {
            // TODO: logging

        }

        redisTemplate.opsForValue().set("order:status:" + uuid, orderStatus.toString());







    }



}
