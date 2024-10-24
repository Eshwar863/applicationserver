package com.example.application.Repo;

import com.example.application.Entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardsRepo extends JpaRepository<Cards, Integer> {
    Cards findBycName(String cName);
}
