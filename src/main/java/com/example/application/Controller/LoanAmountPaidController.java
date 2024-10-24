package com.example.application.Controller;

import com.example.application.DTO.LoanAmountDTO;

import com.example.application.DTO.LoanAmountPaidDTO;
import com.example.application.Service.LoanAmountPaidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("loanPaid")
public class LoanAmountPaidController {

final
LoanAmountPaidService loanAmountPaidService;

    public LoanAmountPaidController(LoanAmountPaidService loanAmountPaidService) {
        this.loanAmountPaidService = loanAmountPaidService;
    }

    @PutMapping("/{LoanId}/amount")
    public ResponseEntity<?> PayLoanAmount(@PathVariable Long LoanId,
                                        @RequestBody Long amount) {
        if (LoanId == null || amount == null || amount <= 0) {
            return new ResponseEntity<>("Enter Valid Details", HttpStatus.BAD_REQUEST);
        }
        LoanAmountDTO loanAmountDTO = loanAmountPaidService.PayLoanAmount(LoanId,amount);
        if (loanAmountDTO == null) {
            return new ResponseEntity<>("Invalid Amount", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(loanAmountDTO, HttpStatus.OK);
    }

    @GetMapping("AllPaidLoans")
    public ResponseEntity<?> getPaidLoans() {
        List<LoanAmountPaidDTO> loanDTOS = loanAmountPaidService.getAllPaidLoans();
        if (loanDTOS == null|| loanDTOS.isEmpty()) {
            return new ResponseEntity<>("No Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{start}/PaidLoans/{end}")
    public ResponseEntity<?> getPaidLoans(@PathVariable(name = "start") LocalDate start,
                                          @PathVariable(name = "end") LocalDate end) {
        if (start.isAfter(end)|| end.isBefore(start)|| start==null || end == null) {
            return new ResponseEntity<>("Enter Valid Date", HttpStatus.BAD_REQUEST);
        }
        List<LoanAmountPaidDTO> loanDTOS = loanAmountPaidService.getPaidLoans(start,end);
        if (loanDTOS == null || loanDTOS.isEmpty()) {
            return new ResponseEntity<>("No Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
