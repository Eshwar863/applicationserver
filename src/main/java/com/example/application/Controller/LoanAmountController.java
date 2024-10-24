package com.example.application.Controller;

import com.example.application.DTO.LoanAmountDTO;
import com.example.application.DTO.LoanDTO;
import com.example.application.Entity.Loans;
import com.example.application.Service.LoanAmountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("Loan")
public class LoanAmountController {
    final
    LoanAmountService loanAmountService;

    public LoanAmountController(LoanAmountService loanAmountService) {
        this.loanAmountService = loanAmountService;
    }



    @PostMapping("LoanToUser")
    public ResponseEntity<?> LoanToUser(@RequestBody LoanDTO loanDTO){
        if(loanDTO == null || loanDTO.getLoanAmount()==0 || loanDTO.getUserName() == null){
            return new ResponseEntity<>("Enter Valid Details", HttpStatus.BAD_REQUEST);
        }
        LoanDTO loanDTO1 = loanAmountService.LoanToUser(loanDTO.getLoanAmount(),loanDTO.getUserName());
        if(loanDTO1==null){
            return new ResponseEntity<>("Request Not Updated", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(loanDTO1, HttpStatus.OK);
    }
    @GetMapping("UpdatedLoans")
    public ResponseEntity<?> LoanAmount1(){
        List<LoanDTO> loanDTOS = loanAmountService.LoansDTO();
        if(loanDTOS==null || loanDTOS.isEmpty()){
            return new ResponseEntity<>("No Updated Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanDTOS, HttpStatus.OK);
    }
    @GetMapping("Loans")
    public ResponseEntity<?> LoanAmount2(){
        List<LoanAmountDTO> loanAmountDTOS = loanAmountService.getAllLoans();
        if(loanAmountDTOS==null || loanAmountDTOS.isEmpty()){
            return new ResponseEntity<>("No Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanAmountDTOS, HttpStatus.OK);
    }

    @GetMapping("{userName}/Loans")
    public ResponseEntity<?> LoansbyUsername(@PathVariable String userName){
        if (userName.isEmpty()){
            return new ResponseEntity<>("Enter User Name", HttpStatus.BAD_REQUEST);
        }
        List<LoanAmountDTO> loanAmountDTOS = loanAmountService.loanByUser(userName);
        if(loanAmountDTOS==null || loanAmountDTOS.isEmpty()){
            return new ResponseEntity<>("No Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanAmountDTOS, HttpStatus.OK);
    }

    @GetMapping("{start}/LoansBTW/{end}")
    public ResponseEntity<?> LoanAmountBTW(@PathVariable(name = "start") LocalDate start,
                                             @PathVariable(name = "end") LocalDate end){

        if (start.isAfter(end) || end.isBefore(start) ||start == null||end == null){
            return new ResponseEntity<>("Enter the Valid Dates", HttpStatus.BAD_REQUEST);
        }

        List<LoanAmountDTO> loanAmountDTOS = loanAmountService.LoanAmountBTW(start,end);
        if(loanAmountDTOS==null || loanAmountDTOS.isEmpty()){
            return new ResponseEntity<>("No Loans Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanAmountDTOS, HttpStatus.OK);
    }
    @GetMapping("{start}/InterestAmountBTW/{end}")
    public ResponseEntity<?> LoanAmountInterestBTW(@PathVariable(name = "start") LocalDate start,
                                             @PathVariable(name = "end") LocalDate end){
        if (start.isAfter(end) || end.isBefore(start)||start == null||end == null){
            return new ResponseEntity<>("Enter the Valid Dates", HttpStatus.BAD_REQUEST);
        }
        Long INAmount = loanAmountService.LoanAmountInterestBTW(start, end);
        if (INAmount == null){
            return new ResponseEntity<>("No Interest Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(INAmount, HttpStatus.OK);
    }
    @GetMapping("InterestAmount/month")
    public ResponseEntity<?> LoanAmountInterestMonth(){
        Long Amount =  loanAmountService.LoanAmountInterestMonth();
        if (Amount == null){
            return new ResponseEntity<>("No Interest Found", HttpStatus.OK);
        }
        return new ResponseEntity<>(Amount, HttpStatus.OK);
    }

//    @PutMapping("/{username}/amount")
//    public LoanAmountDTO PayLoanAmount(@PathVariable String username,
//                                            @RequestBody Long amount) {
//        return loanAmountService.PayLoanAmount(username,amount);
//    }

//    @PostMapping("loanamount")
//    public LoanAmount loanamount(@RequestBody LoanAmount loanAmount){
//       return loanAmountService.loanamount(loanAmount);
//    }
//
//    @GetMapping("Loans")
//    public List<LoanAmount> LoanAmount(){
//      return   loanAmountService.Loans();
//    }
}
