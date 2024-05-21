package com.example.Warehouse.service;


import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.exceptions.ProductNotExistException;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.repository.StorehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class StockService {


    private StockRepository stockRepository;


    private ProductRepository productRepository;


    private StorehouseRepository storehouseRepository;

    @Autowired
    public StockService(StockRepository stockRepository, ProductRepository productRepository, StorehouseRepository storehouseRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.storehouseRepository = storehouseRepository;
    }


    public List<StockDto> getAllStock(){
        List<StockDto> stockDtoList = stockRepository.findAll().stream().map(Stock::toStockDto).toList();
        return stockDtoList;
    }


    public StockDto findStock(String productName) {
        Optional<Stock> checkedStock = stockRepository.findStockByProductName(productName);
        if(!checkedStock.isPresent()){
            throw new ProductNotExistException(productName);
        }
        return checkedStock.get().toStockDto();
    }


    public StockDto updateQuantity(String productName, Integer quantity) {
        Optional<Stock> objectStockOptional = stockRepository.findStockByProductName(productName);
        if(!objectStockOptional.isPresent()){
            throw new ProductNotExistException(productName);
        }
        Stock existStock = objectStockOptional.get();
        existStock.setQuantity(quantity);
        return stockRepository.save(existStock).toStockDto();
    }

    public StockDto addStockIntoStorehouse(String productName, Integer quantity) {
        Stock createProductStock = new Stock(getProductByProductName(productName),quantity,getPrimaryStorehouse());
        return stockRepository.save(createProductStock).toStockDto();
    }

    private Storehouse getPrimaryStorehouse(){
        Optional<Storehouse> defStorehouse = storehouseRepository.findByStorehouseName("M1");
        return defStorehouse.get();
    }

    private Product getProductByProductName(String productName){
        return productRepository.findByProductName(productName)
                 .orElseThrow(() -> new ProductNotExistException(productName));

    }
}
