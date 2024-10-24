package com.example.application.Repo;

import com.example.application.Entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoansRepo extends JpaRepository<Loans, Integer> {

    Loans findById(Long id);

}
