package com.example.Warehouse.service;

import com.example.Warehouse.dto.StorehouseDto;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.exceptions.StorehouseAlreadyExistException;
import com.example.Warehouse.exceptions.StorehouseStockExistsException;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.repository.StorehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StorehouseService {

    private final StorehouseRepository storehouseRepository;

    private final StockRepository stockRepository;

    @Autowired
    public StorehouseService(StorehouseRepository storehouseRepository, StockRepository stockRepository) {
        this.storehouseRepository = storehouseRepository;
        this.stockRepository = stockRepository;
    }

    public List<StorehouseDto> getAllStorehouse() {
        List<StorehouseDto> storehouseDtoList = storehouseRepository.findAll().stream().map(Storehouse::toStorehouseDto).toList();
        return storehouseDtoList;
    }

    public StorehouseDto addStorehouseIntoDB(StorehouseDto storehouseDto) {
        if (storehouseRepository.existsByStorehouseName(storehouseDto.storehouseName())) {
            throw new StorehouseAlreadyExistException(storehouseDto.storehouseName());
        }
        Storehouse storehouse = new Storehouse();
        storehouse.setStorehouseName(storehouseDto.storehouseName());
        storehouse.setAddress(storehouseDto.address());
        return storehouseRepository.save(storehouse).toStorehouseDto();
    }

    @Transactional
    public void deleteStorehouse(String storehouseName) {
        if (hasEmptyStock(storehouseName)) {
            storehouseRepository.findByStorehouseName(storehouseName)
                    .ifPresent(storehouse -> storehouseRepository.delete(storehouse));
        } else {
            throw new StorehouseStockExistsException(storehouseName);
        }
    }

    private boolean hasEmptyStock(String storehouseName) {
        List<Stock> stockList = stockRepository.findStocksByStorehouseName(storehouseName).orElseGet(List::of);
        return stockList.isEmpty();

    }


}
