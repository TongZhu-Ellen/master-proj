package com.tongzhu.order_service.exception;

public class OrderNotFoundException extends RuntimeException{
    final Long id;


    public OrderNotFoundException(Long orderId) {
        this.id = orderId;
    }
}
