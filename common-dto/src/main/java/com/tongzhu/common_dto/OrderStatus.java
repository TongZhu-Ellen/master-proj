package com.tongzhu.common_dto;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PENDING,
    SUCCEED,
    FAILED;
}