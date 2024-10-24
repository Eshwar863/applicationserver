package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Data
@Entity
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cards_Cid", nullable = false)
    private Cards cards;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private String date;


    public String DateFormatter(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");
        return now.format(formatter);
    }
}
