package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongzhu.order_service.dto.ItemsDTO;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "orders")
public class Order {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "JSON")
    private String items; // 直接存JSON字符串



    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime createdAt;


    public Order() {

    }




    public Order(ItemsDTO itemsDTO) throws JsonProcessingException {
        this.items = objectMapper.writeValueAsString(itemsDTO);
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);


    }






    // ----getter----
    public Long getId() {
        return id;
    }

    public String getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }






    // ----setter----
    public void setId(Long id) {
        this.id = id;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
