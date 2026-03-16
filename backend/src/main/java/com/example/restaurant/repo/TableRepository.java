package com.example.restaurant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.model.Table;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByZone(String zone);

    List<Table> findByCapacity(int capacity);
}
