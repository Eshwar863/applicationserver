package com.example.application.Controller;

import com.example.application.Entity.Amounts;
import com.example.application.Service.AmountService;
import com.example.application.Service.CardPaymentService;
import com.example.application.Service.LoanAmountService;
import com.example.application.Service.UsersPaidByMonthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api")
public class AmountsController {

    final
    LoanAmountService loanAmountService;
    final
    AmountService amountService;
    private final CardPaymentService cardPaymentService;

    public AmountsController(LoanAmountService loanAmountService, AmountService amountService, UsersPaidByMonthService usersPaidByMonthService, CardPaymentService cardPaymentService) {
        this.loanAmountService = loanAmountService;
        this.amountService = amountService;
        this.usersPaidByMonthService = usersPaidByMonthService;
        this.cardPaymentService = cardPaymentService;
    }
    final
    UsersPaidByMonthService usersPaidByMonthService;

//    @GetMapping("Totalinterestamount0")
//    public Long getTotalInterest(){
//        return loanAmountService.getTotalInterestAmount(LocalDateTime.now());
//    }

    @GetMapping("Totalinterestamount")
    public ResponseEntity<Long> InterestAmount(){
        LocalDate month = LocalDate.now();
        return new ResponseEntity<>(loanAmountService.InterestAmount(month), HttpStatus.OK);
    }

    @GetMapping("ByDate")
    public ResponseEntity<Amounts> GetByDate(){
        return new ResponseEntity<>(amountService.getByDate(),HttpStatus.OK);
    }
}
