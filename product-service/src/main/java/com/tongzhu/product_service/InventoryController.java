package com.tongzhu.product_service;

import com.tongzhu.product_service.exception.InvalidOrderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ProductService productService;

    public InventoryController(ProductService productService) {
        this.productService = productService;
    }

    @PutMapping("/stocks")
    ResponseEntity<Void> putStocks(@RequestBody ItemsDTO itemsDTO) {

        try {
            productService.patchStocks(itemsDTO);
            return ResponseEntity.noContent().build();
        } catch (InvalidOrderException e) {
            return ResponseEntity.status(409).build();
        }



    }
}
