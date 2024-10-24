package com.example.application.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class Loans {

    @Id
    private Long id;
    private Long loanAmount = 0L;
    private Long interestAmount= 0L;
    @ManyToOne
    @JoinColumn(name = "users_user_id")
    private Users users;
    private LocalDateTime loanDate;

}
