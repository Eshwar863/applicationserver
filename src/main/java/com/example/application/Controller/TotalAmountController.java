package com.example.application.Controller;

import com.example.application.Entity.TotalAmount;
import com.example.application.Service.TotalAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class TotalAmountController {

    final
    TotalAmountService totalAmountService;

    public TotalAmountController(TotalAmountService totalAmountService) {
        this.totalAmountService = totalAmountService;
    }

    @GetMapping("amount")
    public ResponseEntity<Long> getAmount(){
        if(totalAmountService.getAmount() == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(totalAmountService.getAmount(), HttpStatus.OK);

    }
    @PutMapping("updateamount")
    public ResponseEntity<?> addAmount(@RequestBody TotalAmount totalAmount){
        if(totalAmount.getTotalAmount() <=0 ){
            return new ResponseEntity<>("Amount is Lessthan or equalto Zero",HttpStatus.NOT_ACCEPTABLE);
        }
        TotalAmount amount = totalAmountService.addAmount(totalAmount);
        if(amount == null){
            return new ResponseEntity<>("Amount is null",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }
    @GetMapping("alive")
    public String isAlive(){
        return "Server is live";
    }
}
