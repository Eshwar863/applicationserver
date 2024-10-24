package com.example.application.Repo;

import com.example.application.Entity.LoanAmountPaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanAmountPaidRepo extends JpaRepository<LoanAmountPaid, Integer> {
    @Query("select b.amount from LoanAmountPaid b where b.users.userName= :userName")
    Long findLoanAmountPaidByUserName(String userName);
    List<LoanAmountPaid> findByDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
