package com.example.Warehouse.service;


import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.exceptions.ProductAlreadyExistException;
import com.example.Warehouse.exceptions.ProductNotExistException;
import com.example.Warehouse.exceptions.ProductQuantityException;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final StockRepository stockRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public List<ProductDto> getAllRecords() {
        List<ProductDto> productDtoList = productRepository.findAll().stream().map(Product::toProductDto).toList();
        return productDtoList;
    }

    public ProductDto addProduct(ProductDto productDto) {
        if (productRepository.existsByProductName(productDto.productName())) {
            throw new ProductAlreadyExistException(productDto.productName());
        }
        Product entityProduct = new Product();
        entityProduct.setProductName(productDto.productName());
        entityProduct.setPrice(productDto.price());
        ProductDto savedProduct = productRepository.save(entityProduct).toProductDto();
        return savedProduct;
    }

    @Transactional
    public void deleteProduct(String productName) {
        if (productRepository.existsByProductName(productName)) {
            int quantity = getProductQuantity(productName);
            if (isZero(quantity)) {
                deleteStockByProductName(productName);
                productRepository.deleteByProductName(productName);
            } else {
                throw new ProductQuantityException(productName);
            }
        } else {
            throw new ProductNotExistException(productName);
        }
    }

    public ProductDto getProduct(String productName) {
        if (productRepository.findByProductName(productName).isEmpty()) {
            throw new ProductNotExistException(productName);
        }
        return productRepository.findByProductName(productName).get().toProductDto();
    }


    public ProductDto changeProductInfoRecord(String productName, ProductDto productDto) {
        Optional<Product> updateProductInformation = productRepository.findByProductName(productName);
        if (!updateProductInformation.isPresent()) {
            throw new ProductNotExistException(productName);
        } else {
            Product existingProduct = updateProductInformation.get();
            existingProduct.setProductName(productDto.productName());
            existingProduct.setPrice(productDto.price());
            return productRepository.save(existingProduct).toProductDto();
        }
    }

    public List<ProductDto> displayProductsByPriceRange(int minimalPrice, int maximalPrice) {
        checkRangeOfPrice(minimalPrice, maximalPrice);
        List<ProductDto> listByPriceRange = productRepository.findAll().stream()
                .filter(product -> product.getPrice() >= minimalPrice && product.getPrice() <= maximalPrice).toList()
                .stream().map(Product::toProductDto).toList();
        return listByPriceRange;
    }

    public List<ProductDto> sortProductByPrice(String storehouseName) {
        List<ProductDto> productListSortedByPrice = stockRepository.findAll()
                .stream().filter(stock -> stock.getStorehouse().getStorehouseName().equals(storehouseName))
                .map(stock -> stock.getProduct())
                .sorted(Comparator.comparingInt(Product::getPrice))
                .map(Product::toProductDto)
                .toList();

        return productListSortedByPrice;
    }

    public List<ProductDto> sortProductByProductName(String storehouseName) {
        List<ProductDto> productListSortedByProductName = stockRepository.findAll()
                .stream().filter(stock -> stock.getStorehouse().getStorehouseName().equals(storehouseName))
                .map(stock -> stock.getProduct())
                .sorted(Comparator.comparing(Product::getProductName))
                .map(Product::toProductDto)
                .toList();

        return productListSortedByProductName;
    }

    public Map<String, List<ProductDto>> groupProductsByPrice() {
        Map<String, List<ProductDto>> groupingBy = new HashMap<>();
        groupingBy.put("Group up to 10000: ", productRepository.findAll().stream()
                .filter(product -> product.getPrice() <= 10000 && product.getPrice() > 0)
                .map(Product::toProductDto)
                .toList());

        groupingBy.put("Group up to 30000: ", productRepository.findAll().stream()
                .filter(product -> product.getPrice() <= 30000 && product.getPrice() > 10000)
                .map(Product::toProductDto)
                .toList());

        groupingBy.put("Group over 30000: ", productRepository.findAll().stream()
                .filter(product -> product.getPrice() > 30000)
                .map(Product::toProductDto)
                .toList());
        return groupingBy;
    }


    private int getProductQuantity(String productName) {
        Optional<Integer> checkStock = stockRepository.findQuantityByProductName(productName);
        return checkStock.orElse(0);
    }

    private void deleteStockByProductName(String productName) {
        Long checkStockId = stockRepository.findStockIdByProductName(productName).orElseThrow(() -> new ProductNotExistException(productName));
        stockRepository.deleteById(checkStockId);
    }

    private boolean isZero(int quantity) {
        return quantity == 0;
    }

    private void checkRangeOfPrice(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimal value can not be bigger than maximal value");
        } else if (min < 0) {
            throw new IllegalArgumentException("Range of price can not be negative");
        }
    }
}
