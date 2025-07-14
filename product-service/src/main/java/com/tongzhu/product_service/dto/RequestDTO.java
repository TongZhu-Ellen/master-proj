package com.tongzhu.product_service.dto;

import com.tongzhu.product_service.OrderStatus;


import java.io.Serializable;

public record RequestDTO(String uuid,
                         ItemsDTO itemsDTO,
                         OrderStatus orderStatus) implements Serializable {


}





