package com.tongzhu.product_service;




import com.tongzhu.product_service.exception.InvalidOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }













    // this is basically check and deduct stocks;
    // throws InvalidOrderException if anything in any prodId-value pair goes wrong;
    // otherwise will do the deduction;
    // this is transactional because checking and deducting goes together,
    // and will roll back if ANY ONE pair goes wrong;
    @Transactional
    public void putStocks(ItemsDTO itemsDTO) {

        List<Product> updatedProducts = new ArrayList<>();



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
                    updatedProducts.add(p);
                });

        productRepository.saveAll(updatedProducts);




    }
}
