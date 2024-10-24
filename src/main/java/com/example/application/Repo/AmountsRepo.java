package com.example.application.Repo;

import com.example.application.Entity.Amounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountsRepo extends JpaRepository<Amounts, Integer> {
    Amounts findByDate(String date);
}
