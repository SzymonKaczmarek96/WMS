package com.example.Warehouse.service;


import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.StockStreamDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.exceptions.ProductNotExistException;
import com.example.Warehouse.exceptions.StorehouseNotExistException;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.repository.StorehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


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

    public List<StockDto> getAllStock() {
        List<StockDto> stockDtoList = stockRepository.findAll().stream().map(Stock::toStockDto).toList();
        return stockDtoList;
    }

    public StockDto findStock(String productName) {
        Optional<Stock> checkedStock = stockRepository.findStockByProductName(productName);
        if (!checkedStock.isPresent()) {
            throw new ProductNotExistException(productName);
        }
        return checkedStock.get().toStockDto();
    }

    public StockDto updateQuantity(String productName, Integer quantity) {
        Optional<Stock> objectStockOptional = stockRepository.findStockByProductName(productName);
        if (!objectStockOptional.isPresent()) {
            throw new ProductNotExistException(productName);
        }
        Stock existStock = objectStockOptional.get();
        existStock.setQuantity(quantity);
        return stockRepository.save(existStock).toStockDto();
    }

    public StockDto addStockIntoStorehouse(String productName, Integer quantity, String storehouseName) {
        Stock createProductStock = new Stock(getProductByProductName(productName), quantity, getPrimaryStorehouse(storehouseName));
        return stockRepository.save(createProductStock).toStockDto();
    }

    public int sumOfWarehouseStock(String warehouseName) {
        return stockRepository.findAll().stream()
                .filter(stock -> stock.getStorehouse().getStorehouseName().equals(warehouseName))
                .mapToInt(productStock -> productStock.getQuantity()).sum();
    }

    public List<StockStreamDto> displayStockOfProductOnAllStorehouse(String productName) {
        List<Stock> allStockByProductName = stockRepository.findAll().stream()
                .filter(product -> product.getProduct().getProductName().equals(productName)).toList();
        List<Stock> storehouseList = allStockByProductName.stream()
                .map(stock -> new Stock(stock.getQuantity(), stock.getStorehouse())).toList();
        return storehouseList.stream().map(Stock::toStockDtoToStream).toList();
    }

    public List<StockDto> sortStockByQuantity(String storehouseName) {
        List<StockDto> stockListSortedByQuantity = stockRepository.findAll()
                .stream().filter(stock -> stock.getStorehouse().getStorehouseName().equals(storehouseName))
                .sorted(Comparator.comparingInt(stock -> stock.getQuantity()))
                .map(Stock::toStockDto)
                .toList();
        return stockListSortedByQuantity;
    }

    public int sumOfWarehouseStockInTheDesignatedRange(String storehouseName, int min, int max) {
        checkRangeOfPrice(min, max);
        int allStockByStorehouseName = stockRepository.findAll()
                .stream().filter(stock -> stock.getStorehouse().getStorehouseName().equals(storehouseName))
                .filter(product -> product.getProduct().getPrice() >= min && product.getProduct().getPrice() <= max)
                .mapToInt(Stock::getQuantity).sum();
        return allStockByStorehouseName;
    }

    private Storehouse getPrimaryStorehouse(String storehouseName) {
        Storehouse defStorehouse = storehouseRepository.findByStorehouseName(storehouseName)
                .orElseThrow(() -> new StorehouseNotExistException(storehouseName));
        return defStorehouse;
    }

    private Product getProductByProductName(String productName) {
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new ProductNotExistException(productName));

    }

    private void checkRangeOfPrice(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimal value can not be bigger than maximal value");
        } else if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Range of price can not be negative");
        }
    }
}
