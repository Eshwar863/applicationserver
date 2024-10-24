package com.example.application.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Tid;
    private Long amount;
    private String type;
    private LocalDateTime Transactiontime;
    private String TransDescription;


    public String Type() {
        return "Total Updated Amount ";
    }

    public String LoanAmount() {
        return "Loan Amount @1.5 interest";
    }


//    public String DateTime() {
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        String formattedTime = now.format(formatter);
//        return
//    }
}