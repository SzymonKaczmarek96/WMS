package com.example.Warehouse.controller;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.StockStreamDto;
import com.example.Warehouse.entity.Product;
import com.example.Warehouse.entity.Stock;
import com.example.Warehouse.entity.Storehouse;
import com.example.Warehouse.repository.ProductRepository;
import com.example.Warehouse.repository.StockRepository;
import com.example.Warehouse.service.ProductService;
import com.example.Warehouse.service.StockService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StreamDataProcessingTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductService productService;

    @InjectMocks
    private StockService stockService;

    @Test
    public void shouldDisplayProductsByPriceRange() {
        //given
        Product product1 = new Product(20L, "Batteries 300AH", 30000);
        Product product2 = new Product(21L, "Batteries 400AH", 35000);
        Product product3 = new Product(22L, "Batteries 500AH", 50000);
        List<Product> products = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(products);
        //when
        List<ProductDto> productDtoList = productService.displayProductsByPriceRange(30000, 40000);
        //then
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(2, productDtoList.size());

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenMinIsBiggerThanMax() {
        //given
        Product product1 = new Product(20L, "Batteries 300AH", 30000);
        Product product2 = new Product(21L, "Batteries 400AH", 35000);
        Product product3 = new Product(22L, "Batteries 500AH", 50000);
        List<Product> products = Arrays.asList(product1, product2, product3);
        //when
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(40000, 30000));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenMinOrMaxLessThanZero() {
        //given
        Product product1 = new Product(20L, "Batteries 300AH", 30000);
        Product product2 = new Product(21L, "Batteries 400AH", 35000);
        Product product3 = new Product(22L, "Batteries 500AH", 50000);
        List<Product> products = Arrays.asList(product1, product2, product3);
        //when
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(-1, 30000));
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(30000, -1));
    }

    @Test
    public void shouldSortProductListByPrice() {
        //given
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Product product1 = new Product(22L, "Batteries 500AH", 50000);
        Product product2 = new Product(21L, "Batteries 400AH", 35000);
        Product product3 = new Product(20L, "Batteries 300AH", 30000);
        Stock stock1 = new Stock(2L, product1, 50, storehouse);
        Stock stock2 = new Stock(3L, product2, 50, storehouse);
        Stock stock3 = new Stock(4L, product3, 50, storehouse);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        List<ProductDto> productDtoList = productService.sortProductByPrice("M1");
        //given
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(3, productDtoList.size());
        Assertions.assertEquals(product3.getPrice(), productDtoList.get(0).price());
        Assertions.assertEquals(product2.getPrice(), productDtoList.get(1).price());
        Assertions.assertEquals(product1.getPrice(), productDtoList.get(2).price());

    }

    @Test
    public void shouldSortProductListByProductName() {
        //given
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 35000);
        Product product3 = new Product(20L, "Z", 30000);
        Stock stock1 = new Stock(2L, product1, 50, storehouse);
        Stock stock2 = new Stock(3L, product2, 50, storehouse);
        Stock stock3 = new Stock(4L, product3, 50, storehouse);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        List<ProductDto> productDtoList = productService.sortProductByProductName("M1");
        //given
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(3, productDtoList.size());
        Assertions.assertEquals(product1.getPrice(), productDtoList.get(0).price());
        Assertions.assertEquals(product2.getPrice(), productDtoList.get(1).price());
        Assertions.assertEquals(product3.getPrice(), productDtoList.get(2).price());
    }

    @Test
    public void shouldGroupProductsIntoPriceRanges() {
        //given
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 39000);
        Product product3 = new Product(20L, "Z", 20000);
        Product product4 = new Product(19L, "B", 25000);
        Product product5 = new Product(18L, "X", 8000);
        Product product6 = new Product(17L, "Q", 4000);
        List<Product> products = Arrays.asList(product1, product2, product3, product4, product5, product6);
        when(productRepository.findAll()).thenReturn(products);
        //when
        Map<String, List<ProductDto>> groupingBy = productService.groupProductsByPrice();
        //then
        Assertions.assertNotNull(groupingBy);
        Assertions.assertEquals(2, groupingBy.get("Group up to 10000: ").size());
        Assertions.assertEquals(2, groupingBy.get("Group up to 30000: ").size());
        Assertions.assertEquals(2, groupingBy.get("Group over 30000: ").size());
    }

    @Test
    public void shouldSumQuantitiesForChosenStorehouse() {
        //given
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 39000);
        Product product3 = new Product(20L, "Z", 20000);
        Product product4 = new Product(19L, "B", 25000);
        Product product5 = new Product(18L, "X", 8000);
        Product product6 = new Product(17L, "Q", 4000);
        Stock stock1 = new Stock(2L, product1, 60, storehouse);
        Stock stock2 = new Stock(3L, product2, 50, storehouse);
        Stock stock3 = new Stock(4L, product3, 40, storehouse);
        Stock stock4 = new Stock(5L, product4, 50, storehouse);
        Stock stock5 = new Stock(6L, product5, 100, storehouse);
        Stock stock6 = new Stock(7L, product6, 100, storehouse);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3, stock4, stock5, stock6);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        int sum = stockService.sumOfWarehouseStock("M1");
        int sum1 = stockService.sumOfWarehouseStock("M2");
        //then
        Assertions.assertEquals(400, sum);
        Assertions.assertEquals(0, sum1);
    }

    @Test
    public void shouldDisplayStockOfProductOnAllStorehouse() {
        //given
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Storehouse storehouse1 = new Storehouse(2l, "M2", "Street");
        Storehouse storehouse2 = new Storehouse(3l, "M3", "Street");
        Storehouse storehouse3 = new Storehouse(4l, "M4", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Stock stock1 = new Stock(2L, product1, 60, storehouse);
        Stock stock2 = new Stock(3L, product1, 50, storehouse1);
        Stock stock3 = new Stock(4L, product1, 40, storehouse2);
        Stock stock4 = new Stock(5L, product1, 100, storehouse3);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3, stock4);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        List<StockStreamDto> stockStreamDtoList = stockService.displayStockOfProductOnAllStorehouse("A");
        //then
        int quantity = stockStreamDtoList.stream()
                .filter(stockStreamDto1 -> "M2".equals(stockStreamDto1.storehouseName()))
                .map(StockStreamDto::quantity)
                .findFirst().get();
        Assertions.assertNotNull(stockStreamDtoList);
        Assertions.assertEquals(4, stockStreamDtoList.size());
        Assertions.assertEquals(50, quantity);
    }

    @Test
    public void shouldSortStockByQuantity() {
        //then
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 39000);
        Product product3 = new Product(20L, "Z", 20000);
        Product product4 = new Product(19L, "B", 25000);
        Product product5 = new Product(18L, "X", 8000);
        Product product6 = new Product(17L, "Q", 4000);
        Stock stock1 = new Stock(2L, product1, 60, storehouse);
        Stock stock2 = new Stock(3L, product2, 50, storehouse);
        Stock stock3 = new Stock(4L, product3, 40, storehouse);
        Stock stock4 = new Stock(5L, product4, 30, storehouse);
        Stock stock5 = new Stock(6L, product5, 150, storehouse);
        Stock stock6 = new Stock(7L, product6, 200, storehouse);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3, stock4, stock5, stock6);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        List<StockDto> sortedByQuantity = stockService.sortStockByQuantity("M1");
        //then
        Assertions.assertNotNull(sortedByQuantity);
        Assertions.assertEquals(30, sortedByQuantity.get(0).quantity());
        Assertions.assertEquals(40, sortedByQuantity.get(1).quantity());
        Assertions.assertEquals(50, sortedByQuantity.get(2).quantity());
        Assertions.assertEquals(60, sortedByQuantity.get(3).quantity());
        Assertions.assertEquals(150, sortedByQuantity.get(4).quantity());
        Assertions.assertEquals(200, sortedByQuantity.get(5).quantity());
    }

    @Test
    public void shouldSumOfWarehouseStockInTheDesignatedRange() {
        //given
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 39000);
        Product product3 = new Product(20L, "Z", 20000);
        Product product4 = new Product(19L, "B", 25000);
        Product product5 = new Product(18L, "X", 8000);
        Product product6 = new Product(17L, "Q", 4000);
        Stock stock1 = new Stock(2L, product1, 60, storehouse);
        Stock stock2 = new Stock(3L, product2, 50, storehouse);
        Stock stock3 = new Stock(4L, product3, 40, storehouse);
        Stock stock4 = new Stock(5L, product4, 30, storehouse);
        Stock stock5 = new Stock(6L, product5, 150, storehouse);
        Stock stock6 = new Stock(7L, product6, 200, storehouse);
        List<Stock> stocks = Arrays.asList(stock1, stock2, stock3, stock4, stock5, stock6);
        when(stockRepository.findAll()).thenReturn(stocks);
        //when
        int sum = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 10000, 50000);
        int sum1 = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 0, 10000);
        int sum2 = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 30000, 50000);
        //then
        Assertions.assertEquals(180, sum);
        Assertions.assertEquals(350, sum1);
        Assertions.assertEquals(110, sum2);
    }

}
