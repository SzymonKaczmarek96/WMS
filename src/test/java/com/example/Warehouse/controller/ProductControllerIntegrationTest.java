package com.example.Warehouse.controller;


import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@SpringBootTest

public class ProductControllerIntegrationTest extends TestContainer {

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void shouldGetAllProducts() {
        productRepository.deleteAll();
        Product product = new Product(8L, "Batteries 60AH", 50000);
        Product product1 = new Product(10L, "Batteries 70AH", 65000);

        productRepository.save(product);
        productRepository.save(product1);

        List<ProductDto> productDtoList = productController.displayAllProduct().getBody();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), productController.displayAllProduct().getStatusCode());
        Assertions.assertEquals(2, productDtoList.size());

    }

    @Test
    void shouldFindIntroducedProduct() {

        Product product = new Product(1L, "Batteries 45AH", 40000);
        Product product1 = new Product(4L, "Batteries 70AH", 65000);

        productRepository.save(product);
        productRepository.save(product1);

        ProductDto productDto = productController.displayProduct("Batteries 70AH").getBody();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), productController.displayProduct("Batteries 70AH").getStatusCode());
        Assertions.assertEquals(product1.getProductName(), productDto.productName());
        Assertions.assertEquals(product1.getPrice(), productDto.price());
    }


}
