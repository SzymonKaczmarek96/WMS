package com.example.Warehouse.exceptions;

public class ProductQuantityException extends RuntimeException {


    public ProductQuantityException(String productName) {
        super("Product with name '" + productName + "' has more quantity than 0.");
    }

}
