package com.example.Warehouse.entity;
import com.example.Warehouse.dto.StockDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table (name = "stock")
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Stock {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long stock_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @JoinColumn(name = "storehouse_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Storehouse storehouse;

    public StockDto toStockDto(){
        StockDto stockDto = new StockDto(product,quantity,storehouse);
        return stockDto;
    }

    public Stock(Product product, int quantity, Storehouse storehouse) {
        this.product = product;
        this.quantity = quantity;
        this.storehouse = storehouse;
    }

    public void setQuantity(Integer quantity) {

        if(quantity < 0){
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        else {
            this.quantity = quantity;
        }
    }
}


