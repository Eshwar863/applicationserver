package com.example.application.Controller;

import com.example.application.Entity.History;
import com.example.application.Service.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class HistoryController {
    final
    HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("transaction")
    public ResponseEntity<?> allTransactions(){
        List<History> histories =  historyService.getTransactions();
        if(histories.isEmpty()){
            new ResponseEntity<>("No Records Found", HttpStatus.OK);
        }
        return ResponseEntity.ok(histories);
    }

    @GetMapping("cards/transaction")
    public ResponseEntity<?> allCardsTransactions(){
        List<History> cardshistories = historyService.getAllCardtransactions();
        if(cardshistories.isEmpty()){
            new ResponseEntity<>("No Records Found", HttpStatus.OK);
        }
        return ResponseEntity.ok(cardshistories);
    }

    @GetMapping("Loans/transaction")
    public ResponseEntity<?> allLoansTransactions(){
        List<History> Loanshistories = historyService.getAllLoanstransactions();
        if(Loanshistories.isEmpty()){
            new ResponseEntity<>("No Records Found", HttpStatus.OK);
        }
        return ResponseEntity.ok(Loanshistories);
    }

    @GetMapping("InterestUpdated/transaction")
    public ResponseEntity<?> allInterestUpdatedTransactions(){
        List<History> InterestUpdatedhistories = historyService.getAllInterestUpdatedtransactions();
        if(InterestUpdatedhistories.isEmpty()){
            new ResponseEntity<>("No Records Found", HttpStatus.OK);
        }
        return ResponseEntity.ok(InterestUpdatedhistories);
    }

}
