package com.example.application.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Amounts {

    @Id
    private String date;
    private Long Totalamounts;
    private Long totalInterestAmount;
    private Long UserPaidByAmount;
    private Long CardsAmount;

    public String DateFormatter(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");
        return now.format(formatter);
    }
    public String convertDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");
        return date.format(formatter);
    }
}
