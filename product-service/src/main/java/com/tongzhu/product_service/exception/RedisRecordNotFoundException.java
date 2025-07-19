package com.tongzhu.product_service.exception;

public class RedisRecordNotFoundException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Redis record expected but not found. ";
    }

}
