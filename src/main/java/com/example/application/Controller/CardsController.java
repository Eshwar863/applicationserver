package com.example.application.Controller;

import com.example.application.Entity.Cards;
import com.example.application.Service.CardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CardsController {

    final
    CardsService cardsService;

    public CardsController(CardsService cardsService) {
        this.cardsService = cardsService;
    }


    @PostMapping("createcard")
    public ResponseEntity<?> CreateCard(@RequestBody Cards cards){
        if(
                cards==null ||
                cards.getCName() == null  ||
                cards.getCStartingDate() == null||
                cards.getCExpireDate() == null||
                cards.getCType() == null
        ){
            return new ResponseEntity<>("Invalid Card Details",HttpStatus.BAD_REQUEST);
        }
       Cards Createdcard =  cardsService.Create(cards);
        if(Createdcard == null){
            return new ResponseEntity<>("Failed to create card",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Createdcard, HttpStatus.CREATED);
    }
    @PutMapping("updateCard/{cName}")
    public ResponseEntity<?> UpdateCard(@RequestBody Cards cards,
                            @PathVariable (name = "cName")String cName ){
        if (cName.isEmpty()){
            new ResponseEntity<>("Invalid Card Details",HttpStatus.BAD_REQUEST);
        }
        Cards Updatecard = cardsService.updateCard(cards,cName);
        if(Updatecard == null){
            return new ResponseEntity<>("Failed to update card",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Updatecard, HttpStatus.OK);
    }
    @GetMapping("allcards")
    public ResponseEntity<?> GetAllCards(){
         List<Cards> cards = cardsService.getAllCards();
         if (cards.isEmpty()) {
             return new ResponseEntity<>("No cards found",HttpStatus.OK);
         }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

}
