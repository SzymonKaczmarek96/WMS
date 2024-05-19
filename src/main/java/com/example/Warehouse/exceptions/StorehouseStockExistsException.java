package com.example.Warehouse.exceptions;

public class StorehouseStockExistsException extends RuntimeException {
    public StorehouseStockExistsException(String storehouseName) {
        super("Stock in " + storehouseName + " is more than zero");
    }
}
