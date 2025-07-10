package com.tongzhu.order_service;

import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.web.bind.annotation.*;


@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductServiceClient {

    @PutMapping("/api/inventory/stocks")
    void putStocks(@RequestBody ItemsDTO itemsDTO);




}