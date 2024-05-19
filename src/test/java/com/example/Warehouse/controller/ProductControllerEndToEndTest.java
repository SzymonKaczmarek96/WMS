package com.example.Warehouse.controller;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.repository.StorehouseRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ProductControllerEndToEndTest extends TestContainer {

    @LocalServerPort
    private Integer port;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StorehouseRepository storehouseRepository;


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
    }

    @Test
    void shouldGetAllProducts(){

        Product product1 = new Product(20L,"Batterie 200AH",2000000);
        Product product2 = new Product(21L,"Batterie 210AH",2000000);
        Product product3 = new Product(22L,"Batterie 220AH",2000000);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        Response response = RestAssured.get("/products");
            response.then()
                    .statusCode(200)
                    .body("size()", equalTo(3))
                    .body("productName", hasItems("Batterie 200AH","Batterie 210AH","Batterie 220AH"));
        }


    @Test
    void shouldDisplayChosenProduct(){

        Product product4 = new Product(25L,"Batterie 150AH",1500000);

        productRepository.save(product4);

        Response response = RestAssured.get("/products/Batterie 150AH");
        response.then()
                .statusCode(200)
                .body("productName",equalTo("Batterie 150AH"))
                .body("price",equalTo(1500000));
    }


    @Test
    void shouldCreateProduct(){
        ProductDto productDto = new ProductDto("Batterie 150AH", 1500000);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(productDto)// body
                .post("/products/create");

        response.then()
                .statusCode(200)
                .body("productName",equalTo("Batterie 150AH"))
                .body("price",equalTo(1500000));
    }

    @Test
    void shouldDeleteProduct(){
        Product product4 = new Product(1L,"Batterie 200AH",2000000);
        Storehouse storehouse = new Storehouse(1L,"M1","ul.Łochowska 30, 85-372 Bydgoszcz");
        Stock stock = new Stock(1L,product4,0,storehouse);


        productRepository.save(product4);

        storehouseRepository.save(storehouse);


        Assertions.assertEquals(1,productRepository.findAll().size());

        stockRepository.save(stock);

        Response response = RestAssured.delete("/products/delete/Batterie 200AH");

        response.then()
                .statusCode(204);
        Assertions.assertEquals(0,productRepository.findAll().size());

    }




}