package com.tongzhu.common_dto;


import java.io.Serializable;

public record RequestDTO (String uuid,
                         ItemsDTO itemsDTO) implements Serializable {
}
