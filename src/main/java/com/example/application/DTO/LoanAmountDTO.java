package com.example.application.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class LoanAmountDTO {
    private Long loanId;
    private Long loanAmount ;
    private Long interestAmount;
    private LocalDateTime loanDate;

////    public LoanAmountDTO(int loanId, Long loanAmount, Long interestAmount, LocalDateTime loanDate) {
////        this.loanId = loanId;
////        this.loanAmount = loanAmount;
////        this.interestAmount = interestAmount;
////        this.loanDate = loanDate;
//    }


    public LoanAmountDTO(Long loanId,Long loanAmount, Long interestAmount, LocalDateTime loanDate) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.interestAmount = interestAmount;
        this.loanDate = loanDate;
    }

}
