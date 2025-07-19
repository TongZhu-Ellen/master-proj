
package com.tongzhu.product_service.product;



import com.tongzhu.common_dto.ItemsDTO;

import com.tongzhu.common_dto.OrderStatus;
import com.tongzhu.common_dto.RequestDTO;
import com.tongzhu.product_service.PretendedErrorService;
import com.tongzhu.product_service.exception.InvalidOrderException;


import com.tongzhu.product_service.exception.RedisRecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductListener {

    private final ProductService productService;
    private final StringRedisTemplate stringRedisTemplate;
    private final static Logger log = LoggerFactory.getLogger(ProductListener.class);

    public ProductListener(ProductService productService, StringRedisTemplate stringRedisTemplate) {
        this.productService = productService;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @RabbitListener(queues = "request_queue")
    public void hearOrder(RequestDTO requestDTO) {

        String uuid = requestDTO.uuid();
        ItemsDTO itemsDTO = requestDTO.itemsDTO();


        try {


            String orderStatus = stringRedisTemplate.opsForValue().get("order:status:" + uuid);
            if (orderStatus == null) {
                throw new RedisRecordNotFoundException();


            } else if (!"PENDING".equals(orderStatus)) {
                // could potentially mark this down too,
                // but I skip that since re-delivery is quite expected for Rabbit.


            } else {

                productService.handleOrder(itemsDTO);
                stringRedisTemplate.opsForValue().set("order:status:" + uuid,
                        OrderStatus.SUCCEED.toString());
            }



        } catch (InvalidOrderException e) {

            stringRedisTemplate.opsForValue().set("order:status:" + uuid,
                    OrderStatus.FAILED.toString());

        }  catch (RuntimeException e) {

            PretendedErrorService.pretendToPersistError(uuid, e.getMessage());
        }












    }


}
