package com.example.application.DTO;

import java.time.LocalDateTime;

public class LoanAmountPaidDTO {
    private Integer loanId;
    private Long loanAmount ;
    private String UserName ;
    private LocalDateTime loanPaidDate;

    public LoanAmountPaidDTO(Integer loanId, Long loanAmount, String userName, LocalDateTime loanPaidDate) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        UserName = userName;
        this.loanPaidDate = loanPaidDate;
    }
}
