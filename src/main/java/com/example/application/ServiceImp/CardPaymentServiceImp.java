package com.example.application.ServiceImp;

import com.example.application.DTO.CardPaymentDTO;

import java.util.List;

public interface CardPaymentServiceImp {

    CardPaymentDTO addAmount(String cName, String monthYear, Long amount);

    List<Object[]> getAmountByMonth(String cName);

    Long getAmount();

    CardPaymentDTO getAmountByMonth(String cName, String month);
}
