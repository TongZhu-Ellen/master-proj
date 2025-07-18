package com.tongzhu.product_service.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tongzhu.common_dto.ItemsDTO;
import com.tongzhu.common_dto.OrderStatus;
import com.tongzhu.common_dto.RequestDTO;
import com.tongzhu.product_service.exception.InvalidOrderException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductListener {

    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    public ProductListener(ProductService productService, RabbitTemplate rabbitTemplate) {
        this.productService = productService;
        this.rabbitTemplate = rabbitTemplate;
    }


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
            productService.handleOrder(itemsDTO);
            orderStatus = OrderStatus.SUCCEED;
        } catch (InvalidOrderException e) {
            orderStatus = OrderStatus.FAILED;
        }
        rabbitTemplate.convertAndSend("response_queue",
                new RequestDTO(uuid, itemsDTO, orderStatus));



    }


}
