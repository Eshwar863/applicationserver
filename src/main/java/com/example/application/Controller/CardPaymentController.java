package com.example.application.Controller;

import com.example.application.DTO.CardPaymentDTO;
import com.example.application.Service.CardPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("CardPayment")
public class CardPaymentController {
    final
    CardPaymentService cardPaymentService;

    public CardPaymentController(CardPaymentService cardPaymentService) {
        this.cardPaymentService = cardPaymentService;
    }

    @PutMapping("{cName}/addamount/{monthYear}")
    public ResponseEntity<?> addAmount(@PathVariable(name = "cName") String cName,
                                    @PathVariable(name = "monthYear") String monthYear,
                                    @RequestBody Long amount) {
        if(amount == null || amount <= 0) {
            return new ResponseEntity<>("Invalid Amount",HttpStatus.BAD_REQUEST);
        }
        CardPaymentDTO cardPaymentDTO =  cardPaymentService.addAmount(cName,monthYear,amount);
        if(cardPaymentDTO == null) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cardPaymentDTO);
    }
    @GetMapping("{cName}/getAllamount")
    public ResponseEntity<List<Object[]>> getAllCardPayments(@PathVariable(name = "cName") String cName) {
        return new ResponseEntity<>(cardPaymentService.getAmountByMonth(cName), HttpStatus.OK);
    }

//    @Scheduled(cron ="0 0 * * * *")
    @GetMapping("totalCardsamount")
    public ResponseEntity<Long> getTotalCardsAmount(){
        return new ResponseEntity<>(cardPaymentService.getAmount(), HttpStatus.OK);
    }

    @GetMapping("{cName}/getamount/{month}")
    public ResponseEntity<?> getCardPaymentsByMonth(@PathVariable(name = "cName") String cName, @PathVariable(name = "month") String month) {
        CardPaymentDTO cardPaymentDTO = cardPaymentService.getAmountByMonth(cName,month);
        if(cardPaymentDTO == null) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cardPaymentDTO, HttpStatus.OK);
    }

}
