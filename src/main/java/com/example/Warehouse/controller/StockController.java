package com.example.Warehouse.controller;


import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.TotalStorehouseQuantityDto;
import com.example.Warehouse.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<StockDto>> displayStockList() {
        List<StockDto> stockList = stockService.getAllStock();
        return ResponseEntity.ok().body(stockList);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<StockDto> findStockByProductName(@PathVariable String productName) {
        StockDto foundStock = stockService.findStock(productName);
        return ResponseEntity.ok(foundStock);
    }

    @GetMapping("/sum/{storehouseName}")
    public ResponseEntity<Integer> sumStockAllProductInStorehouse(@PathVariable String storehouseName) {
        Integer productStockSum = stockService.sumOfWarehouseStock(storehouseName);
        return ResponseEntity.ok(productStockSum);
    }

    @GetMapping("/sort/quantity")
    public ResponseEntity<List<StockDto>> getProductListInStorehouseSortByStock(@RequestParam("storehouse") String storehouseName) {
        List<StockDto> stockDtoList = stockService.sortStockByQuantity(storehouseName);
        return ResponseEntity.ok(stockDtoList);
    }

    @GetMapping("check/{productName}")
    public ResponseEntity<List<TotalStorehouseQuantityDto>> selectProductStock(@PathVariable String productName) {
        List<TotalStorehouseQuantityDto> totalStorehouseQuantityDtoList = stockService.displayStockOfProductOnAllStorehouse(productName);
        return ResponseEntity.ok(totalStorehouseQuantityDtoList);
    }

    @GetMapping("select/{storehouseName}")
    public ResponseEntity<Integer> selectProductStockInRange(@PathVariable String storehouseName, @RequestParam int min, @RequestParam int max) {
        int sumOfProductStockInRange = stockService.sumOfWarehouseStockInTheDesignatedRange(storehouseName, min, max);
        return ResponseEntity.ok(sumOfProductStockInRange);
    }

    @PutMapping("/update/{productName}")
    public ResponseEntity<StockDto> updateStock(@PathVariable String productName, @RequestBody Integer quantity) {
        StockDto updatedQuantity = stockService.updateQuantity(productName, quantity);
        return ResponseEntity.ok(updatedQuantity);
    }

    @PostMapping("/create")
    public ResponseEntity<StockDto> createStock(@RequestBody StockDto stockDto) {
        StockDto addedStockIntoStorehouse = stockService.addStockIntoStorehouse(stockDto.product().getProductName(), stockDto.quantity(), stockDto.storehouse().getStorehouseName());
        return ResponseEntity.ok(addedStockIntoStorehouse);
    }
}
