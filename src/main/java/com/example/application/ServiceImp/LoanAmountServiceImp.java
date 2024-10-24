package com.example.application.ServiceImp;

import com.example.application.DTO.LoanAmountDTO;
import com.example.application.DTO.LoanDTO;
import com.example.application.Entity.LoanAmount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LoanAmountServiceImp {

    LoanDTO LoanToUser(Long loanAmount , String userName);
    List<LoanDTO> LoansDTO();
    List<LoanAmountDTO> loanByUser(String userName);
    void updateInterestForAllLoans();
    //Long getTotalInterestAmount(LocalDateTime date);
    void Loans(LoanAmount loanAmount);
    List<LoanAmountDTO> LoanAmountBTW(LocalDate start, LocalDate end);
    Long LoanAmountInterestBTW(LocalDate start, LocalDate end);
    Long LoanAmountInterestMonth();
    Long InterestAmount(LocalDate month);

}
