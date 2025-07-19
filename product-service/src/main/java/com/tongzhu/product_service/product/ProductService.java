package com.tongzhu.product_service.product;








import com.tongzhu.common_dto.*;

import com.tongzhu.product_service.exception.InvalidOrderException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




@Service
public class ProductService {


    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional
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