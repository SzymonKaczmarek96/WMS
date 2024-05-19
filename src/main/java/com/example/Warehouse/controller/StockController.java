package com.example.Warehouse.controller;


import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.service.StockService;
import com.example.Warehouse.service.StorehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    @GetMapping
    public ResponseEntity<List<StockDto>> displayStockList(){
        List<StockDto> stockList = stockService.getAllStock();
        return ResponseEntity.ok().body(stockList);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<StockDto> findStockByProductName(@PathVariable String productName){
        StockDto foundStock = stockService.findStock(productName);
        return ResponseEntity.ok(foundStock);
    }

    @PutMapping("/update/{productName}/{quantity}")
    public ResponseEntity<StockDto> updateStock(@PathVariable String productName,@PathVariable Integer quantity){
        StockDto updatedQuantity = stockService.updateQuantity(productName,quantity);
        return ResponseEntity.ok(updatedQuantity);
    }

    @PostMapping("/create/{productName}/{quantity}")
    public ResponseEntity<StockDto> createStock(@PathVariable String productName,@PathVariable Integer quantity){
        StockDto addedStockIntoStorehouse = stockService.addStockIntoStorehouse(productName,quantity);
        return ResponseEntity.ok(addedStockIntoStorehouse);
    }




}
