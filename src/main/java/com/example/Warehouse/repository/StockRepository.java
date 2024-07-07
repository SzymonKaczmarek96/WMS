package com.example.Warehouse.repository;

import com.example.Warehouse.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s.quantity FROM Stock s WHERE s.product.productName = :productName")
    Optional<Integer> findQuantityByProductName(String productName);

    @Query("SELECT s.stock_id FROM Stock s WHERE s.product.productName = :productName")
    Optional<Long> findStockIdByProductName(String productName);

    @Query("SELECT s FROM Stock s WHERE s.product.productName = :productName")
    Optional<Stock> findStockByProductName(String productName);

    @Query("SELECT m FROM Stock m WHERE m.storehouse.storehouseName = :storehouseName")
    Optional<List<Stock>> findStocksByStorehouseName(String storehouseName);

}
