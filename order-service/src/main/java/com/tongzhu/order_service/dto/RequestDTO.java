package com.tongzhu.order_service.dto;

import com.tongzhu.order_service.OrderStatus;

public record RequestDTO(String uuid,
                         ItemsDTO itemsDTO,
                         OrderStatus orderStatus) {
}
