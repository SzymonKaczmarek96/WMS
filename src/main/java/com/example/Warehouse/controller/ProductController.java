package com.example.Warehouse.controller;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> displayAllProduct() {
        List<ProductDto> productList = productService.getAllRecords();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductDto> displayProduct(@PathVariable String productName) {
        ProductDto productDto = productService.getProduct(productName);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/sort/price")
    public ResponseEntity<List<ProductDto>> getProductListInStorehouseSortByPrice(@RequestParam("storehouse") String storehouseName) {
        List<ProductDto> productDtoList = productService.sortProductByPrice(storehouseName);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/sort/name")
    public ResponseEntity<List<ProductDto>> getProductListInStorehouseSortByProductName(@RequestParam("storehouse") String storehouseName) {
        List<ProductDto> productDtoList = productService.sortProductByProductName(storehouseName);
        return ResponseEntity.ok(productDtoList);
    }


    @GetMapping("/check/price")
    public ResponseEntity<List<ProductDto>> displayProductByPriceRange(@RequestParam int min, @RequestParam int max) {
        List<ProductDto> productDtoList = productService.displayProductsByPriceRange(min, max);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/grouping")
    public ResponseEntity<Map<String, List<ProductDto>>> displayProductByGroupingPrice() {
        Map<String, List<ProductDto>> grouped = productService.groupProductsByPrice();
        return ResponseEntity.ok(grouped);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        ProductDto createProduct = productService.addProduct(productDto);
        return ResponseEntity.ok().body(createProduct);
    }

    @DeleteMapping("/delete/{productName}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String productName) {
        productService.deleteProduct(productName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{productName}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productName, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.changeProductInfoRecord(productName, productDto);
        return ResponseEntity.ok().body(updatedProduct);
    }

}
