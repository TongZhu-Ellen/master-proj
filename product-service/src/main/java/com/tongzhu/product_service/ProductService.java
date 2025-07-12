package com.tongzhu.product_service;




import com.tongzhu.product_service.dto.ItemsDTO;
import com.tongzhu.product_service.dto.RequestDTO;
import com.tongzhu.product_service.dto.ResponseDTO;
import com.tongzhu.product_service.exception.InvalidOrderException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }












    @RabbitListener(queues = "request_queue")
    public void hearOrder(RequestDTO requestDTO) {
        try {
            handleOrder(requestDTO.itemsDTO());
            rabbitTemplate.convertAndSend("response_queue",
                    new ResponseDTO(requestDTO.requestId(), true));

        } catch (InvalidOrderException e) {
            rabbitTemplate.convertAndSend("response_queue",
                    new ResponseDTO(requestDTO.requestId(), false));

        }
    }



    @Transactional
    public void handleOrder(ItemsDTO itemsDTO) {

        itemsDTO.keySet().stream()
                .sorted()
                .forEach(id -> {
                    Product p = productRepository.findByIdForUpdate(id)
                            .orElseThrow(() -> new InvalidOrderException());
                    int wantAmount = itemsDTO.get(id);
                    int stockAmount = p.getStock();
                    if (wantAmount > stockAmount) throw new InvalidOrderException();

                    int newStock = stockAmount - wantAmount;
                    p.setStock(newStock);


                });






    }
}
