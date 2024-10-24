package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class LoanAmount {

    @Id
    private Long id;
    private Long loanAmount = 0L;
    private Long interestAmount= 0L;
    @ManyToOne
    @JoinColumn(name = "users_user_id")
    private Users users;
    private LocalDateTime loanDate;
    private LocalDateTime lastInterestUpdate;

    public String Date123() {
        return "2024-08-27T17:53:24.5482616";
    }


}
