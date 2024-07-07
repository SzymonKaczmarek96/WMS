package com.example.Warehouse.controller;

import com.example.Warehouse.dto.ProductDto;
import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.TotalStorehouseQuantityDto;
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
        when(productRepository.findAll()).thenReturn(productGenerator());
        //when
        List<ProductDto> productDtoList = productService.displayProductsByPriceRange(30000, 40000);
        //then
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(1, productDtoList.size());

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenMinIsBiggerThanMax() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(40000, 30000));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenMinOrMaxLessThanZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(-1, 30000));
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.displayProductsByPriceRange(30000, -1));
    }

    @Test
    public void shouldSortProductListByPrice() {
        //given
        when(stockRepository.findAll()).thenReturn(stockGenerator());
        //when
        List<ProductDto> productDtoList = productService.sortProductByPrice("M1");
        //given
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(6, productDtoList.size());
    }

    @Test
    public void shouldSortProductListByProductName() {
        //given
        when(stockRepository.findAll()).thenReturn(stockGenerator());
        //when
        List<ProductDto> productDtoList = productService.sortProductByProductName("M1");
        //given
        Assertions.assertNotNull(productDtoList);
        Assertions.assertEquals(6, productDtoList.size());
    }

    @Test
    public void shouldGroupProductsIntoPriceRanges() {
        //given
        when(productRepository.findAll()).thenReturn(productGenerator());
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
        when(stockRepository.findAll()).thenReturn(stockGenerator());
        //when
        int sum = stockService.sumOfWarehouseStock("M1");
        int sum1 = stockService.sumOfWarehouseStock("M2");
        //then
        Assertions.assertEquals(530, sum);
        Assertions.assertEquals(0, sum1);
    }

    @Test
    public void shouldDisplayStockOfProductOnAllStorehouse() {
        //given
        when(stockRepository.findAll()).thenReturn(generateStocksForDifferentStorehouses());
        //when
        List<TotalStorehouseQuantityDto> totalStorehouseQuantityDtoList = stockService.displayStockOfProductOnAllStorehouse("A");
        //then
        int quantity = totalStorehouseQuantityDtoList.stream()
                .filter(totalStorehouseQuantityDto1 -> "M2".equals(totalStorehouseQuantityDto1.storehouseName()))
                .map(TotalStorehouseQuantityDto::quantity)
                .findFirst().get();
        Assertions.assertNotNull(totalStorehouseQuantityDtoList);
        Assertions.assertEquals(4, totalStorehouseQuantityDtoList.size());
        Assertions.assertEquals(50, quantity);
    }

    @Test
    public void shouldSortStockByQuantity() {
        //then
        when(stockRepository.findAll()).thenReturn(stockGenerator());
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
        when(stockRepository.findAll()).thenReturn(stockGenerator());
        //when
        int sum = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 10000, 50000);
        int sum1 = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 0, 10000);
        int sum2 = stockService.sumOfWarehouseStockInTheDesignatedRange("M1", 30000, 50000);
        //then
        Assertions.assertEquals(180, sum);
        Assertions.assertEquals(350, sum1);
        Assertions.assertEquals(110, sum2);
    }

    private List<Stock> stockGenerator(){
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
       return Arrays.asList(stock1, stock2, stock3, stock4, stock5, stock6);
    }

    private List<Product> productGenerator(){
        Product product1 = new Product(22L, "A", 50000);
        Product product2 = new Product(21L, "T", 39000);
        Product product3 = new Product(20L, "Z", 20000);
        Product product4 = new Product(19L, "B", 25000);
        Product product5 = new Product(18L, "X", 8000);
        Product product6 = new Product(17L, "Q", 4000);
        return Arrays.asList(product1, product2, product3, product4, product5, product6);
    }

    private List<Stock> generateStocksForDifferentStorehouses(){
        Storehouse storehouse = new Storehouse(1l, "M1", "Street");
        Storehouse storehouse1 = new Storehouse(2l, "M2", "Street");
        Storehouse storehouse2 = new Storehouse(3l, "M3", "Street");
        Storehouse storehouse3 = new Storehouse(4l, "M4", "Street");
        Product product1 = new Product(22L, "A", 50000);
        Stock stock1 = new Stock(2L, product1, 60, storehouse);
        Stock stock2 = new Stock(3L, product1, 50, storehouse1);
        Stock stock3 = new Stock(4L, product1, 40, storehouse2);
        Stock stock4 = new Stock(5L, product1, 100, storehouse3);
        return Arrays.asList(stock1, stock2, stock3, stock4);

    }
}
