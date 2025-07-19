package com.tongzhu.order_service.order;



import com.tongzhu.common_dto.*;

import com.tongzhu.order_service.PretendedErrorService;
import com.tongzhu.order_service.exception.OrderNotFoundException;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.stereotype.Service;

import java.util.UUID;





@Service
public class OrderService {

    private final StringRedisTemplate redisTemplate;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final static Logger log = LoggerFactory.getLogger(OrderService.class);





    public OrderService(StringRedisTemplate redisTemplate,
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






    public String sendOrder(ItemsDTO itemsDTO) {
        String uuid = UUID.randomUUID().toString();


       try {
           redisTemplate.opsForValue().set("order:status:" + uuid,
                   OrderStatus.PENDING.toString());


           rabbitTemplate.convertAndSend("request_queue",
                   new RequestDTO(uuid, itemsDTO));
       } catch (RuntimeException e) {

           PretendedErrorService.pretendToPersistError(uuid, e.getMessage());



       }

        return uuid;

    }











}
