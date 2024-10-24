package com.example.application.Service;

import com.example.application.DTO.LoanAmountDTO;
import com.example.application.DTO.LoanAmountPaidDTO;
import com.example.application.Entity.*;
import com.example.application.Exceptions.LoanAmountExceeded;
import com.example.application.Exceptions.LoanNotFound;
import com.example.application.Exceptions.UserNotFound;
import com.example.application.Repo.*;
import com.example.application.ServiceImp.LoanAmountPaidServiceImp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanAmountPaidService implements LoanAmountPaidServiceImp {
    final
    LoanAmountRepo loanAmountRepo;
    final
    TotalAmountRepo totalAmountRepo;
    final
    UserRepo userRepo;
    final
    LoanAmountPaidRepo loanAmountPaidRepo;
    private final HistoryRepo historyRepo;

    public LoanAmountPaidService(LoanAmountRepo loanAmountRepo, TotalAmountRepo totalAmountRepo, UserRepo userRepo, LoanAmountPaidRepo loanAmountPaidRepo, HistoryRepo historyRepo) {
        this.loanAmountRepo = loanAmountRepo;
        this.totalAmountRepo = totalAmountRepo;
        this.userRepo = userRepo;
        this.loanAmountPaidRepo = loanAmountPaidRepo;
        this.historyRepo = historyRepo;
    }

//    public LoanAmountPaid PayLoanAmount(String username, Long amount) {
//    }

    public LoanAmountDTO PayLoanAmount(Long LoanId, Long amount) {
        Users retrivedusers = retriveLoggedInUser();
        LoanAmount amount1 = loanAmountRepo.findById(Math.toIntExact(LoanId)).orElseThrow(
                ()->   new UserNotFound(String.format("Loan Id %d Not found", LoanId))
        );
        if(amount > amount1.getLoanAmount()){
            throw new LoanAmountExceeded(String.format("Loan Amount Exceeded %d",amount));
        }
        LoanAmountPaid loanAmountPaid = new LoanAmountPaid();
          loanAmountPaid.setId(Math.toIntExact(amount1.getId()));
          loanAmountPaid.setDate(LocalDateTime.now());
          loanAmountPaid.setUsers(amount1.getUsers());
          loanAmountPaid.setAmount(amount);
          loanAmountPaidRepo.save(loanAmountPaid);

        History  history = new History();
        history.setTransactiontime(LocalDateTime.now());
        history.setType("Loans");
        history.setTransDescription(String.format("%s paid Loan amount ₹%d", amount1.getUsers().getUserName(), amount));
        history.setAmount(amount);
        historyRepo.save(history);
        UpdateTotalAmount(LoanId,amount);
          RemainingLoanAmount(LoanId,amount,amount1.getLoanAmount());
        return new LoanAmountDTO(amount1.getId(),loanAmountPaid.getAmount(),amount1.getInterestAmount(),loanAmountPaid.getDate());
    }

    public void RemainingLoanAmount(Long LoanId, Long amount,Long loanAmount) {

        Long RemainingLoanAmount = loanAmount - amount;
        LoanAmount  loanAmount1 = loanAmountRepo.findById(Math.toIntExact(LoanId)).orElseThrow(
                () -> new UserNotFound(String.format("Loan Id %d Not found", LoanId))
        );
        loanAmount1.setLoanAmount(RemainingLoanAmount);
        loanAmountRepo.save(loanAmount1);

    }

    public void UpdateTotalAmount(Long LoanId , Long amount) {

        TotalAmount tamount = totalAmountRepo.findAll().get(0);
        tamount.setTotalAmount(tamount.getTotalAmount()+amount);
        tamount.setDate(LocalDateTime.now());
        totalAmountRepo.save(tamount);
        History  history = new History();
        history.setTransactiontime(LocalDateTime.now());
        history.setType("Totals");
        history.setAmount(tamount.getTotalAmount());
        history.setTransDescription(String.format
                ("Updated Total amount where loan Amount is ₹%d of loanid %d",amount,LoanId));
        historyRepo.save(history);
    }
    public List<LoanAmountPaidDTO> getAllPaidLoans(){
        Users retrivedusers = retriveLoggedInUser();
        List<LoanAmountPaid> loanAmountPaid = loanAmountPaidRepo.findAll();
        return loanAmountPaid.stream()
                .map(loans -> new LoanAmountPaidDTO(
                        loans.getId(),
                        loans.getAmount(),
                        loans.getUsers().getUserName(),
                        loans.getDate()
                )).collect(Collectors.toList());
    }


    public List<LoanAmountPaidDTO> getPaidLoans(LocalDate start, LocalDate end){
        Users retrivedusers = retriveLoggedInUser();
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        List<LoanAmountPaid> loanAmountPaids =loanAmountPaidRepo.findByDateBetween(startDateTime,endDateTime);
        if (loanAmountPaids.isEmpty()){
            throw new LoanNotFound(String.format("No Loans Found between %s and %s",start,end));
        }
        return loanAmountPaids.stream()
                .map(loans -> new LoanAmountPaidDTO(
                        loans.getId(),
                        loans.getAmount(),
                        loans.getUsers().getUserName(),
                        loans.getDate()
                )).collect(Collectors.toList());
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

//
//List<LoanAmount> loanAmounts  = loanAmountRepo.findByDateBetween(startDateTime,endDateTime);
//       if (loanAmounts.isEmpty()){
//        throw new LoanNotFound(String.format("No Loans Found between %s and %s",start,end));
//        }
//        return loanAmounts.stream()
//                .map(loan -> new LoanAmountDTO(
//        loan.getId(),
//                        loan.getLoanAmount(),
//                        loan.getInterestAmount(),
//                        loan.getLoanDate()))
//        .collect(Collectors.toList());
//        }

