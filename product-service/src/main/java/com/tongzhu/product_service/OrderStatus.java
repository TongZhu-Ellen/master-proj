package com.tongzhu.product_service;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PENDING,
    SUCCEED,
    FAILED;
}
