package com.example.application.ServiceImp;

import com.example.application.Entity.History;

import java.util.List;

public interface HistoryServiceImp {

    List<History> getTransactions();
    List<History> getAllCardtransactions();
    List<History> getAllLoanstransactions();
    List<History> getAllInterestUpdatedtransactions();

}
