package com.example.Warehouse.dto;

import com.example.Warehouse.entity.Product;
import com.example.Warehouse.entity.Storehouse;

public record StockDto(Product product, int quantity, Storehouse storehouse) {

}

