package com.tongzhu.product_service.product;





import com.fasterxml.jackson.core.JsonProcessingException;


import com.tongzhu.common_dto.*;

import com.tongzhu.product_service.exception.InvalidOrderException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;




    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {

        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Transactional
    @RabbitListener(queues = "request_queue")
    public void hearOrder(RequestDTO requestDTO) throws JsonProcessingException {

        String uuid = requestDTO.uuid();
        ItemsDTO itemsDTO = requestDTO.itemsDTO();
        OrderStatus orderStatus = requestDTO.orderStatus();

        if (!OrderStatus.PENDING.equals(orderStatus)) {

            // TODO: logging here

            return;
        }

        try {
            handleOrder(itemsDTO);
            orderStatus = OrderStatus.SUCCEED;
        } catch (InvalidOrderException e) {
            orderStatus = OrderStatus.FAILED;
        }
        rabbitTemplate.convertAndSend("response_queue",
                new RequestDTO(uuid, itemsDTO, orderStatus));



    }


    public void handleOrder(ItemsDTO itemsDTO) {

        List<Long> sortedKeys = new ArrayList<>(itemsDTO.keySet());
        Collections.sort(sortedKeys);

        for (Long id : sortedKeys) {
            Product p = productRepository.findByIdForUpdate(id)
                    .orElseThrow(() -> new InvalidOrderException());
            int curStock = p.getStock();
            int wantAmount = itemsDTO.get(id);
            if (curStock < wantAmount) throw new InvalidOrderException();
            else p.setStock(curStock - wantAmount);

        }





    }


}




