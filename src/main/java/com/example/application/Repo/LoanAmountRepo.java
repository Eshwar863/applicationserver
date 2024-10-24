package com.example.application.Repo;

import com.example.application.Entity.LoanAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanAmountRepo extends JpaRepository<LoanAmount, Integer> {
    @Query("select a from LoanAmount a where a.users.userName= :userName")
    List<LoanAmount> findLoanAmountByuserName(String userName);
    @Query("select b from LoanAmount b where b.loanDate<=:date")
    List<LoanAmount> findAllByDate(LocalDateTime date);
    @Query("select b from LoanAmount b where b.loanDate between :start and :end")
    List<LoanAmount> findByDateBetween(LocalDateTime start, LocalDateTime end);
    @Query("select b from LoanAmount b where b.lastInterestUpdate between :start and :end ")
    List<LoanAmount> findInterestAmount(LocalDateTime start, LocalDateTime end);
}
