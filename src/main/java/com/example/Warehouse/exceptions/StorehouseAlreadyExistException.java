package com.example.Warehouse.exceptions;

public class StorehouseAlreadyExistException extends RuntimeException{

    public StorehouseAlreadyExistException(String storehouseName) {
        super("Storehouse with name" + storehouseName + "already exist");
    }
}
