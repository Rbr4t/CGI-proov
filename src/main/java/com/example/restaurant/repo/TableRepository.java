package com.example.restaurant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restaurant.model.Table;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findfindByZone(String zone);

    List<Table> findByCapacity(int capacity);
}
