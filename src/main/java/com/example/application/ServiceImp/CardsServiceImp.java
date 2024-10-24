package com.example.application.ServiceImp;

import com.example.application.Entity.Cards;

import java.util.List;

public interface CardsServiceImp {

    Cards Create(Cards cards);
    Cards updateCard(Cards cards, String cName);
    List<Cards> getAllCards();

}
