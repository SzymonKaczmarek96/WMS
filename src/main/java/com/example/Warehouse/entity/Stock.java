package com.example.Warehouse.entity;

import com.example.Warehouse.dto.StockDto;
import com.example.Warehouse.dto.TotalStorehouseQuantityDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "stock")
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stock_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @Getter
    private Product product;

    @Getter
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @JoinColumn(name = "storehouse_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @Getter
    private Storehouse storehouse;

    public StockDto toStockDto() {
        return new StockDto(product, quantity, storehouse);
    }

    public TotalStorehouseQuantityDto toStockDtoToStream() {
        return new TotalStorehouseQuantityDto(quantity, storehouse.getStorehouseName());
    }

    public Stock(Product product, int quantity, Storehouse storehouse) {
        this.product = product;
        this.quantity = quantity;
        this.storehouse = storehouse;
    }

    public Stock(int quantity, Storehouse storehouse) {
        this.quantity = quantity;
        this.storehouse = storehouse;
    }

    public void setQuantity(Integer quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else {
            this.quantity = quantity;
        }
    }
}


