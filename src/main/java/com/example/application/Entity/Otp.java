package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String Otp;
    @Column(nullable = false)
    private LocalDateTime ValidTill;
    @OneToOne
    private Users users;

}
