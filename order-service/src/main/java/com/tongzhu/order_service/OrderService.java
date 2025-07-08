package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongzhu.order_service.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional
    public Order postOrder(ItemsDTO itemsDTO) throws JsonProcessingException {

        // TODO!!! 这里需要检查你的dto是不是标准，然后库存足不足？？？

        Order order = new Order(itemsDTO);
        orderRepository.save(order);

        return order;

    }


}
