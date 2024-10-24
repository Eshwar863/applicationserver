package com.example.application.Service;

import com.example.application.DTO.CardPaymentDTO;
import com.example.application.Entity.*;
import com.example.application.Exceptions.CardNotFount;
import com.example.application.Repo.*;
import com.example.application.ServiceImp.CardPaymentServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardPaymentService implements CardPaymentServiceImp {
    final
    UserRepo userRepo;
    final
    HistoryRepo historyRepo;
    final
    AmountsRepo amountsRepo;
    final
    CardsRepo cardsRepo;
    final
    CardPaymentRepo cardPaymentRepo;
    History history = new History();

    public CardPaymentService(HistoryRepo historyRepo, AmountsRepo amountsRepo, CardsRepo cardsRepo, CardPaymentRepo cardPaymentRepo, UserRepo userRepo) {
        this.historyRepo = historyRepo;
        this.amountsRepo = amountsRepo;
        this.cardsRepo = cardsRepo;
        this.cardPaymentRepo = cardPaymentRepo;
        this.userRepo = userRepo;
    }

    public CardPaymentDTO addAmount(String cName, String monthYear, Long amount) {
        Users retrivedusers = retriveLoggedInUser();
        Cards cards = cardsRepo.findBycName(cName);
        if(cards == null) {
            throw new CardNotFount(String.format("Card %s not found", cName));
        }
        CardPayment  cardPayment = cardPaymentRepo.findByCards_CNameAndDate(cName,monthYear);
        if (cardPayment == null) {
            CardPayment cardPayment1 = new CardPayment();
            cardPayment1.setCards(cards);
            cardPayment1.setDate(monthYear);
            cardPayment1.setAmount(amount);
            cardPaymentRepo.save(cardPayment1);
            history(cardPayment1.getAmount() ,String.format("Card %s with amount ₹%d on %s",cardPayment1.getCards().getCName(),cardPayment1.getAmount(),cardPayment1.getDate()));            return new CardPaymentDTO(cardPayment1.getAmount(),cardPayment1.getDate(),cards.getCName());
        }
        cardPayment.setAmount(amount);
        cardPayment.setDate(monthYear);
        history(cardPayment.getAmount(),String.format("Card %s with amount ₹%d on %s",cardPayment.getCards().getCName(),cardPayment.getAmount(),cardPayment.getDate()));
        cardPaymentRepo.save(cardPayment);
        return new CardPaymentDTO(cardPayment.getAmount(),cardPayment.getDate(),cards.getCName());
    }

    public List<Object[]> getAmountByMonth(String cName) {
        Users retrivedusers = retriveLoggedInUser();
        Cards cards = cardsRepo.findBycName(cName);
        if(cards == null){
            throw  new CardNotFount(String.format("Card %s Doesn't Exist",cName));
        }
        return cardPaymentRepo.findAmountByCName(cName);
    }
    private void history(Long amount ,String Type){
        history.setTransactiontime(LocalDateTime.now());
        history.setAmount(amount);
        history.setTransDescription(Type);
        history.setType("Cards");
        historyRepo.save(history);
    }

    public Long getAmount() {

        Users retrivedusers = retriveLoggedInUser();
        List<CardPayment> cardPayment =
                cardPaymentRepo.findAllByDate(
                        new CardPayment().DateFormatter());
        if(cardPayment == null || cardPayment.isEmpty()){
            return 0L;
        }
        Long amount = cardPayment.stream()
                .mapToLong(CardPayment::getAmount)
                .sum();

        Amounts amounts = amountsRepo.findByDate(new CardPayment().DateFormatter());
        if(amounts == null){
            Amounts amounts1 = new Amounts();
            amounts1.setDate(new UsersPaidByMonth().DateFormatter());
            amounts1.setCardsAmount(amount);
            amounts1.setTotalamounts(amount);
            amountsRepo.save(amounts1);
        }
        amounts.setCardsAmount(amount);
        amountsRepo.save(amounts);
        return amount;
        }

    public CardPaymentDTO getAmountByMonth(String cName, String month) {
        Users retrivedusers = retriveLoggedInUser();
        CardPayment payment = cardPaymentRepo.findByCards_CNameAndDate(cName,month);
        return new CardPaymentDTO(payment.getAmount(), payment.getDate(),payment.getCards().getCName());
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

}
