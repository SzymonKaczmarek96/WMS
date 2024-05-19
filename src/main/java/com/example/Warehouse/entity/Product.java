package com.example.Warehouse.entity;

import com.example.Warehouse.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Table (name = "product")
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;


    @Column(name = "product_name", unique = true, nullable = false)
    private String productName;


    @Column(name = "price",nullable = false)
    private int price;

    public ProductDto toProductDto(){
        ProductDto productDto = new ProductDto(productName,price);
        return productDto;
    }

    public void setPrice(int price) {

        if(price < 0){
            throw new IllegalArgumentException("Price can't be less than 0");
        }
        this.price = price;
    }

}
