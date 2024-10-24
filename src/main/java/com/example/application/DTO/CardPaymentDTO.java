package com.example.application.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CardPaymentDTO {
    private Long amount;
    private String date;
    private String cName;




    public CardPaymentDTO(Long amount, String date, String cName) {
        this.amount = amount;
        this.date = date;
        this.cName = cName;
    }
}
