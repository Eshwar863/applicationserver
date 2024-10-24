package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Setter
@Getter
@Entity
public class UsersPaidByMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(name = "month_year")
    private String monthYear;
    @Column(name = "paid")
    private Boolean paid = false;


    public String DateFormatter(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");
        return now.format(formatter);
    }
}

