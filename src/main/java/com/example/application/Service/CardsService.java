package com.example.application.Service;

import com.example.application.Entity.Cards;
import com.example.application.Entity.History;
import com.example.application.Entity.Users;
import com.example.application.Exceptions.CardAlreadyExist;
import com.example.application.Exceptions.CardNotFount;
import com.example.application.Repo.CardsRepo;
import com.example.application.Repo.HistoryRepo;
import com.example.application.Repo.UserRepo;
import com.example.application.ServiceImp.CardsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CardsService implements CardsServiceImp {
    final
    UserRepo userRepo;
    final
    HistoryRepo historyRepo;
    private final CardsRepo cardsRepo;

    public CardsService(CardsRepo cardsRepo, HistoryRepo historyRepo, UserRepo userRepo) {
        this.cardsRepo = cardsRepo;
        this.historyRepo = historyRepo;
        this.userRepo = userRepo;
    }

    public Cards Create(Cards cards) {
        Users retrivedusers = retriveLoggedInUser();
        Cards cardsIf = cardsRepo.findBycName(cards.getCName());
        if (cardsIf != null) {
            throw  new CardAlreadyExist(String.format("Cards with name %s already exist", cards.getCName()));
        }
        History history = new History();
        history.setType("Cards");
        history.setTransDescription(String.format("Created Card %s of ₹%s", cards.getCName(),cards.getCType()));
        history.setAmount(0L);
        history.setTransactiontime(LocalDateTime.now());
        historyRepo.save(history);
        return cardsRepo.save(cards);
    }

    public Cards updateCard(Cards cards, String cName) {
        Users retrivedusers = retriveLoggedInUser();
        Cards cards1 = cardsRepo.findBycName(cName);
        if(cards1 == null) throw new CardNotFount(String.format("Card %s not found", cName));
        cards1.setCName(Optional.ofNullable(cards.getCName()).orElse(cards1.getCName()));
        cards1.setCType(Optional.ofNullable(cards.getCType()).orElse(cards1.getCType()));
        cards1.setCStartingDate(Optional.ofNullable(cards.getCStartingDate()).orElse(cards1.getCStartingDate()));
        cards1.setCExpireDate(Optional.ofNullable(cards.getCExpireDate()).orElse(cards1.getCExpireDate()));
        History history = new History();
        history.setType("Cards");
        history.setTransDescription(String.format("Updated Card %s of ₹%s", cards1.getCName(),cards1.getCType()));
        history.setAmount(0L);
        history.setTransactiontime(LocalDateTime.now());
        historyRepo.save(history);
        return cardsRepo.save(cards1);
    }

    public List<Cards> getAllCards() {
        Users retrivedusers = retriveLoggedInUser();
        return cardsRepo.findAll();
    }
    private Users retriveLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
            throw new BadCredentialsException("Bad Credentials login ");
        String username = authentication.getName();
        System.out.println("In Logged In User "+username);
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
    }

//    public String m(String cName) {
//        Cards cards = new Cards();
//        cards.setCName(cName);
//        cards.setCStartingDate((java.sql.Date) new Date());
//        cards.setCExpireDate((java.sql.Date) new Date());
//        cards.setCType("cards.getCType()");
//        cardsRepo.save(cards);
//        return cards.getCName();
//    }
}
