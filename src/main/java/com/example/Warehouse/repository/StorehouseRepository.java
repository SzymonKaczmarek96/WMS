package com.example.Warehouse.repository;

import com.example.Warehouse.entity.Storehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StorehouseRepository extends JpaRepository<Storehouse,Long> {

    Optional<Storehouse> findByStorehouseName(String storehouseName);

    boolean existsByStorehouseName(String storehouseName);


}
