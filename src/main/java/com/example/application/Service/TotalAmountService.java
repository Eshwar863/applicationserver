package com.example.application.Service;

import com.example.application.Entity.TotalAmount;
import com.example.application.Entity.History;
import com.example.application.Entity.Users;
import com.example.application.Repo.TotalAmountRepo;
import com.example.application.Repo.HistoryRepo;
import com.example.application.Repo.UserRepo;
import com.example.application.ServiceImp.TotalAmountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class TotalAmountService  implements TotalAmountServiceImp {
    @Autowired
    UserRepo userRepo;
    final
    HistoryRepo historyRepo;
    final
    TotalAmountRepo totalAmountRepo;

    public TotalAmountService(HistoryRepo historyRepo, TotalAmountRepo totalAmountRepo, UserRepo userRepo) {
        this.historyRepo = historyRepo;
        this.totalAmountRepo = totalAmountRepo;
    }




    public TotalAmount addAmount(TotalAmount totalAmount) {
        Users retrivedusers = retriveLoggedInUser();

        List<TotalAmount> totalAmountList = totalAmountRepo.findAll();
        if (totalAmountList.isEmpty()) {
            TotalAmount newTotalAmount = new TotalAmount();
            History historyifEmpty = new History();
            newTotalAmount.setDate(LocalDateTime.now());
            historyifEmpty.setTransactiontime(LocalDateTime.now());
            newTotalAmount.setTotalAmount(totalAmount.getTotalAmount());
            historyifEmpty.setAmount(totalAmount.getTotalAmount());
            historyifEmpty.setTransDescription(String.format("Entered Amount ₹%d",totalAmount.getTotalAmount()));
            historyifEmpty.setType("Totalamount");
            historyRepo.save(historyifEmpty);
            return totalAmountRepo.save(newTotalAmount);

        } else {
            History historyifExist = new History();
            TotalAmount existingTotalAmount = totalAmountList.getFirst();
            existingTotalAmount.setTotalAmount(totalAmount.getTotalAmount());
            historyifExist.setAmount(totalAmount.getTotalAmount());
            existingTotalAmount.setDate(LocalDateTime.now());
            historyifExist.setTransactiontime(LocalDateTime.now());
            historyifExist.setTransDescription(String.format("Updated Total Amount from ₹%d to  ₹%d",
                    totalAmountList.getLast().getTotalAmount(),existingTotalAmount.getTotalAmount()
            ));
            historyifExist.setType("Totalamount");
            historyRepo.save(historyifExist);
            return totalAmountRepo.save(existingTotalAmount);
        }}




    private Users retriveLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
            throw new BadCredentialsException("Bad Credentials login ");
        String username = authentication.getName();
        System.out.println("In Logged In User "+username);
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
    }
    public Long getAmount() {
        return totalAmountRepo.findAmount();
    }
}


//    public Amount addAmount(Amount amount) {
//
//        List<Amount> amount1 = amountRepo.findAll();
//        amountRepo.findAll();
//        if(amountRepo.findAll() == null){
//            Amount amount2 = new Amount();
//            amount2.setDate(LocalDateTime.now());
//            amount2.setTotalAmount(amount.getTotalAmount());
//            return amountRepo.save(amount2);
//
//        }
//        else {
//            Amount existingAmount = amount1.get(0);
//            existingAmount.setTotalAmount(amount.getTotalAmount());
//            existingAmount.setDate(LocalDateTime.now());
//            return amountRepo.save(existingAmount);
//        }
//        History history = new History();
//        history.setAmount(Double.valueOf(amount.getTotalAmount()));
//        history.setTransactiontime(LocalDateTime.now());
//        history.setTransactionType(history.Type());
//        historyRepo.save(history);
//    public Amount updateAmount(){
//        Amount  amount1 = (Amount) amountRepo.findAll();
//        if(amount1 == null){
//            amount1.setTotalAmount(amount.getTotalAmount());
//            amount1.setDate(LocalDateTime.now());
//        }
//    }}
