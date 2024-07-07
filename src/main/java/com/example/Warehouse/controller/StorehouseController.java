package com.example.Warehouse.controller;


import com.example.Warehouse.dto.StorehouseDto;
import com.example.Warehouse.service.StorehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storehouse")
public class StorehouseController {

    private final StorehouseService storehouseService;

    @Autowired
    public StorehouseController(StorehouseService storehouseService) {
        this.storehouseService = storehouseService;
    }

    @GetMapping
    ResponseEntity<List<StorehouseDto>> displayAllStorehouse() {
        List<StorehouseDto> storehouseList = storehouseService.getAllStorehouse();
        return ResponseEntity.ok(storehouseList);
    }

    @PostMapping("/create")
    ResponseEntity<StorehouseDto> createStorehouse(@RequestBody StorehouseDto storehouseDto) {
        StorehouseDto createdDto = storehouseService.addStorehouseIntoDB(storehouseDto);
        return ResponseEntity.ok().body(createdDto);
    }

    @DeleteMapping("/delete/{storehouseName}")
    ResponseEntity<StorehouseDto> deleteStorehouse(@PathVariable String storehouseName) {
        storehouseService.deleteStorehouse(storehouseName);
        return ResponseEntity.noContent().build();

    }


}
