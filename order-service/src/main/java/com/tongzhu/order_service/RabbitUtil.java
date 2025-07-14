package com.tongzhu.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongzhu.order_service.dto.RequestDTO;

public class RabbitUtil {

    static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToString(RequestDTO requestDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(requestDTO);

    }

    public static RequestDTO readFromString(String string) throws JsonProcessingException {
        return objectMapper.readValue(string, RequestDTO.class);
    }
}
