package com.example.Warehouse.exceptions;

public class ProductAlreadyExistException extends RuntimeException {

    public ProductAlreadyExistException(String productName) {
        super("Product with name '" + productName + "' already exists.");
    }
}
