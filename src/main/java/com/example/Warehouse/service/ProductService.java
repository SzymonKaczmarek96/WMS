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


import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private  StockRepository stockRepository;


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
        if(productRepository.existsByProductName(productDto.productName())){
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
        if(productRepository.existsByProductName(productName)){
            int quantity = getProductQuantity(productName);
            if(isZero(quantity)) {
                deleteStockByProductName(productName);
                productRepository.deleteByProductName(productName);
            }
            else {
                throw new ProductQuantityException(productName);
            }
        }else {
            throw new ProductNotExistException(productName);
        }
    }

    public ProductDto getProduct(String productName) {
        ProductDto productDto = productRepository.findByProductName(productName).get().toProductDto();
        return productDto;
    }


    public ProductDto changeProductInfoRecord(String productName, ProductDto productDto) {
        Optional<Product> updateProductInformation = productRepository.findByProductName(productName);

        if(!updateProductInformation.isPresent()){
            throw new ProductNotExistException(productName);
        }
        else {
            Product existingProduct = updateProductInformation.get();
            existingProduct.setProductName(productDto.productName());
            existingProduct.setPrice(productDto.price());
            return productRepository.save(existingProduct).toProductDto();
        }
    }

    private int getProductQuantity(String productName){
        Optional<Integer> checkStock = stockRepository.findQuantityByProductName(productName);
        int stock = checkStock.orElse(0);
        return stock;
    }

    private void deleteStockByProductName(String productName){
        Optional<Long> checkStockId = stockRepository.findStockIdByProductName(productName);
        stockRepository.deleteById(checkStockId.get());
    }

    private boolean isZero(int quantity){
        return quantity == 0;
    }
}
