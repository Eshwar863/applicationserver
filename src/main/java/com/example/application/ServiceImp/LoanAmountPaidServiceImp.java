package com.example.application.ServiceImp;

import com.example.application.DTO.LoanAmountDTO;
import com.example.application.DTO.LoanAmountPaidDTO;

import java.time.LocalDate;
import java.util.List;

public interface LoanAmountPaidServiceImp {

    LoanAmountDTO PayLoanAmount(Long LoanId, Long amount);

    void RemainingLoanAmount(Long LoanId, Long amount,Long loanAmount);

    List<LoanAmountPaidDTO> getPaidLoans(LocalDate start, LocalDate end);

    List<LoanAmountPaidDTO> getAllPaidLoans();
}
