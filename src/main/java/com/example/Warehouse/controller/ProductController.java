package com.example.Warehouse.controller;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/products")
public class ProductController {

    private ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> displayAllProduct(){
        List<ProductDto> productList = productService.getAllRecords();
        return ResponseEntity.ok(productList);
    }
    @GetMapping("/{productName}")
    public ResponseEntity<ProductDto> displayProduct(@PathVariable String productName){
        ProductDto productDto = productService.getProduct(productName);
        return ResponseEntity.ok(productDto);
    }
    @PostMapping("/create")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto){
        ProductDto createProduct = productService.addProduct(productDto);
        return ResponseEntity.ok().body(createProduct);
    }
    @DeleteMapping("/delete/{productName}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String productName){
       productService.deleteProduct(productName);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{productName}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productName,@RequestBody ProductDto productDto){
        ProductDto updatedProduct = productService.changeProductInfoRecord(productName,productDto);
        return ResponseEntity.ok().body(updatedProduct);
    }

}
