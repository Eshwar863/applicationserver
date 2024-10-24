package com.example.application.Service;

import com.example.application.Entity.History;
import com.example.application.Entity.Users;
import com.example.application.Repo.HistoryRepo;
import com.example.application.Repo.UserRepo;
import com.example.application.ServiceImp.HistoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService implements HistoryServiceImp {
    final
    UserRepo userRepo;
    final
    HistoryRepo historyRepo;

    public HistoryService(HistoryRepo historyRepo, UserRepo userRepo) {
        this.historyRepo = historyRepo;
        this.userRepo = userRepo;
    }

    public List<History> getTransactions() {
        Users retrivedusers = retriveLoggedInUser();
        return historyRepo.findAll();
    }

    public List<History> getAllCardtransactions() {
        Users retrivedusers = retriveLoggedInUser();
        return historyRepo.findByTransactionType("Cards");}


        public List<History> getAllLoanstransactions() {
            Users retrivedusers = retriveLoggedInUser();
            return historyRepo.findByTransactionType("Loans");
        }

        public List<History> getAllInterestUpdatedtransactions() {
            Users retrivedusers = retriveLoggedInUser();
            return historyRepo.findByTransactionType("InterestUpdated");
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

