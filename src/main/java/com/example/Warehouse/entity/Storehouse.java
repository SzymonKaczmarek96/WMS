package com.example.Warehouse.entity;


import com.example.Warehouse.dto.StorehouseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "storehouse")
@Entity
public class Storehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storehouse_id")
    private long storehouseId;


    @Column(name = "storehouse_name", unique = true, nullable = false)
    private String storehouseName;


    @Column(name = "address", unique = true, nullable = false)
    private String address;

    public StorehouseDto toStorehouseDto() {
        StorehouseDto storehouseDto = new StorehouseDto(storehouseName, address);
        return storehouseDto;
    }


}
