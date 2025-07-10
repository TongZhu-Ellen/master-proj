package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongzhu.order_service.exception.OrderNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;



    public OrderService(OrderRepository orderRepository, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());
    }

    // @Transactional
    // this is very ironic here, since whatever we do vie Feign is not rollbackable;
    // let's avoid the irony : (
    public Order postOrder(ItemsDTO itemsDTO) throws JsonProcessingException {


        productServiceClient.putStocks(itemsDTO);

        Order order = new Order(itemsDTO);
        orderRepository.save(order);
        return order;



    }


}
