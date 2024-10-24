package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class LoanAmountPaid {
    @Id
    private Integer id;
    private Long amount;
    @ManyToOne
    @JoinColumn(name = "users_user_id")
    private Users users;
    private LocalDateTime date;

}
