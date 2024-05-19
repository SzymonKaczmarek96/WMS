package com.example.Warehouse.exceptions;

public class StorehouseNotExistException extends RuntimeException {

    public StorehouseNotExistException(String storehouseName) {
        super("Storehouse with" + storehouseName + "doesn't exists");
    }
}
