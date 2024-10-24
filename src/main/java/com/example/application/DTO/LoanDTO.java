package com.example.application.DTO;

import lombok.Data;


import java.time.LocalDateTime;
@Data
public class LoanDTO{
    private Long id;
    private Long loanAmount;
    private String userName;
    private LocalDateTime loanDate;
    private Long interestAmount;
    private LocalDateTime lastInterestUpdate;

    public LoanDTO(Long id, Long loanAmount, String userName, LocalDateTime loanDate, Long interestAmount, LocalDateTime lastInterestUpdate) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.userName = userName;
        this.loanDate = loanDate;
        this.interestAmount = interestAmount;
        this.lastInterestUpdate = lastInterestUpdate;
    }
}

