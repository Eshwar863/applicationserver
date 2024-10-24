package com.example.application.Repo;

import com.example.application.Entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepo extends JpaRepository<History,Integer> {
    @Query("select b from History b where b.type = :TransactionType")
    List<History> findByTransactionType(String TransactionType);
}
