package com.example.Warehouse.service;

import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.StorehouseDto;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.exceptions.StorehouseAlreadyExistException;
import com.example.Warehouse.exceptions.StorehouseNotExistException;
import com.example.Warehouse.exceptions.StorehouseStockExistsException;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.repository.StorehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StorehouseService {

    private StorehouseRepository storehouseRepository;

    private StockRepository stockRepository;

    @Autowired
    public StorehouseService(StorehouseRepository storehouseRepository,StockRepository stockRepository) {
        this.storehouseRepository = storehouseRepository;
        this.stockRepository = stockRepository;
    }

    public List<StorehouseDto> getAllStorehouse(){
        List<StorehouseDto> storehouseDtoList = storehouseRepository.findAll().stream().map(Storehouse::toStorehouseDto).toList();
        return storehouseDtoList;
    }

    public StorehouseDto addStorehouseIntoDB(StorehouseDto storehouseDto) {
        if(storehouseRepository.existsByStorehouseName(storehouseDto.storehouseName())){
            throw new StorehouseAlreadyExistException(storehouseDto.storehouseName());
        }
        Storehouse storehouse = new Storehouse();
        storehouse.setStorehouseName(storehouseDto.storehouseName());
        storehouse.setAddress(storehouseDto.address());
        return storehouseRepository.save(storehouse).toStorehouseDto();
    }


    @Transactional
    public void deleteStorehouse(String storehouseName) {
        if(storehouseRepository.existsByStorehouseName(storehouseName)){
            if(checkStock(storehouseName)){
                storehouseRepository.delete(storehouseRepository.findByStorehouseName(storehouseName).get());
            }
            else {
                throw new StorehouseStockExistsException(storehouseName);
            }
        }
        else {
            throw new StorehouseNotExistException(storehouseName);
        }
    }

    private boolean checkStock(String storehouseName){
        if(!storehouseRepository.findByStorehouseName(storehouseName).isPresent()){
            return true;
        }
        else {
            List<Stock> stockList = stockRepository.findStocksByStorehouseName(storehouseName).get();
            if(stockList.size() > 0){
                return false;
            }
        }
        return true;
    }


}
