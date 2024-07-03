package com.example.Warehouse.controller;

import com.example.Warehouse.entity.Product;
import com.example.Warehouse.exceptions.ProductQuantityException;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDeleteTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void shouldDeleteProductWithZeroQuantity() {

        Product product = new Product(1L, "Batteries 45AH", 40000);


        when(productRepository.existsByProductName(product.getProductName())).thenReturn(true);
        when(stockRepository.findQuantityByProductName(product.getProductName())).thenReturn(Optional.of(0));
        when(stockRepository.findStockIdByProductName(product.getProductName())).thenReturn(Optional.of(2L));


        productService.deleteProduct(product.getProductName());


        verify(productRepository, times(1)).existsByProductName(product.getProductName());
        verify(stockRepository, times(1)).findQuantityByProductName(product.getProductName());
        verify(stockRepository, times(1)).findStockIdByProductName(product.getProductName());
        verify(productRepository, times(1)).deleteByProductName(product.getProductName());

    }

    @Test
    void shouldNotDeleteProductWithPositiveQuantity() {
        Product product = new Product(4L, "Batteries 70AH", 65000);

        when(productRepository.existsByProductName(product.getProductName())).thenReturn(true);
        when(stockRepository.findQuantityByProductName(product.getProductName())).thenReturn(Optional.of(100));
        assertThrows(ProductQuantityException.class, () -> productService.deleteProduct(product.getProductName()));
    }


}
