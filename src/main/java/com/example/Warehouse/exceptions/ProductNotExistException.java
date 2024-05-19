package com.example.Warehouse.exceptions;

public class ProductNotExistException extends RuntimeException{

    public ProductNotExistException(String productName){
        super("Product with name '" + productName + "' doesn't exist");
    }
}
