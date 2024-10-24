package com.example.application.Repo;

import com.example.application.Entity.TotalAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TotalAmountRepo extends JpaRepository<TotalAmount, Integer> {
    @Query("select a.totalAmount from TotalAmount a")
    Long findAmount();

    List<TotalAmount> findAll();
}
