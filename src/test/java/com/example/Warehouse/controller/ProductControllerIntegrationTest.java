package com.example.Warehouse.controller;


import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PathVariable;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest

public class ProductControllerIntegrationTest extends TestContainer{

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        Product product = new Product(1L, "Batteries 45AH", 40000);
        Product product1 = new Product(4L, "Batteries 70AH", 65000);

        productRepository.save(product);
        productRepository.save(product1);

        List<ProductDto> productDtoList = productController.displayAllProduct().getBody();

        System.out.println(productDtoList);

        Assertions.assertEquals(HttpStatusCode.valueOf(200),productController.displayAllProduct().getStatusCode());
        Assertions.assertEquals(2,productDtoList.size());
        Assertions.assertEquals(product.toProductDto(),productDtoList.get(0));
        Assertions.assertEquals(product1.toProductDto(),productDtoList.get(1));
    }

    @Test
    void shouldFindIntroducedProduct(){

        Product product = new Product(1L, "Batteries 45AH", 40000);
        Product product1 = new Product(4L, "Batteries 70AH", 65000);

        productRepository.save(product);
        productRepository.save(product1);

        ProductDto productDto = productController.displayProduct("Batteries 70AH").getBody();

        Assertions.assertEquals(HttpStatusCode.valueOf(200),productController.displayProduct("Batteries 70AH").getStatusCode());
        Assertions.assertEquals(product1.getProductName(),productDto.productName());
        Assertions.assertEquals(product1.getPrice(),productDto.price());
    }
}
