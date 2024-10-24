package com.example.application.Service;

import com.example.application.DTO.LoanAmountDTO;
import com.example.application.DTO.LoanDTO;
import com.example.application.Entity.*;
import com.example.application.Exceptions.LoanNotFound;
import com.example.application.Exceptions.UserNotFound;
import com.example.application.Repo.*;
import com.example.application.ServiceImp.LoanAmountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class LoanAmountService implements LoanAmountServiceImp {
    final
    HistoryRepo historyRepo;
    final
    LoanAmountRepo loanAmountRepo;
    final
    UserRepo userRepo;
    final
    LoansRepo loansRepo;
    private final AmountsRepo amountsRepo;
    private final TotalAmountRepo totalAmountRepo;

    public LoanAmountService(HistoryRepo historyRepo, LoanAmountRepo loanAmountRepo, UserRepo userRepo, LoansRepo loansRepo, AmountsRepo amountsRepo, TotalAmountRepo totalAmountRepo) {
        this.historyRepo = historyRepo;
        this.loanAmountRepo = loanAmountRepo;
        this.userRepo = userRepo;
        this.loansRepo = loansRepo;
        this.amountsRepo = amountsRepo;
        this.totalAmountRepo = totalAmountRepo;
    }

//    public LoanAmount loanamount(LoanAmount loanAmount) {
//        history.setTransactionType(history.LoanAmount());
//        history.setTransactiontime(LocalDateTime.now());
//        history.setAmount(Double.valueOf(loanAmount.getLoanAmount()));
//        historyRepo.save(history);
//      Long amount1 = ((long) ((loanAmount.getLoanAmount()) * 1.5)) / 100;
//        loanAmount.setInterestAmount(amount1);
//        return  loanAmountRepo.save(loanAmount);
//    }


        public LoanDTO LoanToUser(Long loanAmount , String userName) {
            Users retrivedusers = retriveLoggedInUser();
            Users users = userRepo.findByUserName(userName).orElseThrow(
                    () -> new UserNotFound(String.format("UserName %s Doesn't Exist",userName)));
            TotalAmount amount = totalAmountRepo.findAll().get(0);

            if(amount.getTotalAmount() <= 0L){
                throw new RuntimeException("Total amount is less than 0!");
            } else if (amount.getTotalAmount() < loanAmount){
                throw new RuntimeException("Total amount is Less than loan amount!");
            }
            LoanAmount loanAmount1 = new LoanAmount();
            loanAmount1.setId(Math.round( 10+ Math.random() * 9000));
            loanAmount1.setLoanAmount(loanAmount);
            loanAmount1.setLoanDate(LocalDateTime.now());
            loanAmount1.setLastInterestUpdate(LocalDateTime.now());
            Long amount1 = ((long) (loanAmount* 1.5)) / 100;
            loanAmount1.setInterestAmount(amount1);
            loanAmount1.setUsers(users);
            History history1 = new History();
            history1.setType("Loans");
            history1.setTransDescription(String.format("Loan given to %s @1.5 interest ",userName));
            history1.setTransactiontime(LocalDateTime.now());
            history1.setAmount(loanAmount);
            Loans(loanAmount1);
            amount.setTotalAmount(amount.getTotalAmount() - loanAmount);
            amount.setDate(LocalDateTime.now());
            totalAmountRepo.save(amount);
            historyRepo.save(history1);
            loanAmountRepo.save(loanAmount1);
            return new LoanDTO(loanAmount1.getId(),loanAmount1.getLoanAmount(),userName,loanAmount1.getLoanDate(),loanAmount1.getInterestAmount(),loanAmount1.getLastInterestUpdate());

        }

    public List<LoanDTO> LoansDTO() {
        Users retrivedusers = retriveLoggedInUser();
        List<LoanAmount> loanAmounts = loanAmountRepo.findAll();

        return loanAmounts.stream().map(loanAmount -> {
            String userName = loanAmount.getUsers().getUserName();
            return new LoanDTO(
                    loanAmount.getId(),
                    loanAmount.getLoanAmount(),
                    userName,
                    loanAmount.getLoanDate(),
                    loanAmount.getInterestAmount(),
                    loanAmount.getLastInterestUpdate()
            );
        }).collect(Collectors.toList());
    }

    public List<LoanAmountDTO> loanByUser(String userName) {
        Users retrivedusers = retriveLoggedInUser();
        Users users = userRepo.findByUserName(userName).orElseThrow(
                () -> new UserNotFound(String.format("UserName %s Doesn't Exist",userName)));
          List<LoanAmount> amounts =   loanAmountRepo.findLoanAmountByuserName(userName);

        List<LoanAmountDTO> loanDTOList = amounts.stream().map(amount -> new LoanAmountDTO(
                amount.getId(),
                amount.getLoanAmount(),
                amount.getInterestAmount(),
                amount.getLoanDate()
        )).collect(Collectors.toList());

        return loanDTOList;
    }


    public void updateInterestForAllLoans() {
        // Get the current date and time
       // LocalDateTime now1 = LocalDateTime.parse("2024-10-01T00:00:00.000000000");
//        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.parse("2024-10-01T00:00:00.000000000");
   // System.out.println(now);
        // Check if today is the 1st of the month and the time is exactly 00:00
        if (!(now.getDayOfMonth() == 1 && now.toLocalTime().truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.MIDNIGHT))) {
            // If it's not the 1st of the month at 00:00, return and do nothing
            return;
        }

        // Fetch all loan amounts from the repository
        List<LoanAmount> loanAmounts = loanAmountRepo.findAll();

        for (LoanAmount loanAmount : loanAmounts) {
            Long currentBalance = loanAmount.getLoanAmount();

            // Skip if the loan amount is zero
            if (currentBalance == 0) {
                continue;
            }
            Loans loans = loansRepo.findById(loanAmount.getId());
            // Since it's the 1st of the month, apply the interest regardless of the last interest update
            Long interestAmount = ((long) (currentBalance * 1.5)) / 100;
            loanAmount.setInterestAmount(interestAmount);

            // Update the last interest calculation time to the current time
            loanAmount.setLastInterestUpdate(now);
            loans.setInterestAmount(interestAmount + loans.getInterestAmount());

            // Save the updated loan
            loansRepo.save(loans);
            loanAmountRepo.save(loanAmount);

            // Log interest application in history
            History history1 = new History();
            history1.setTransDescription(String.format("Interest applied for user %s with LoanId %d", loanAmount.getUsers().getUserName(), loanAmount.getId()));
            history1.setTransactiontime(now);
            history1.setType("InterestUpdated");
            history1.setAmount(loanAmount.getInterestAmount());
            historyRepo.save(history1);
        }
    }



    public Long InterestAmount(LocalDate month){
        Users retrivedusers = retriveLoggedInUser();
        LocalDateTime Date = month.atStartOfDay();
        LocalDateTime endDateTime = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);
        List<LoanAmount> amount = loanAmountRepo.findInterestAmount(Date,endDateTime);
        if(amount.isEmpty()){
            return 0L;
        }
        Long totalInterestAmount = amount.stream()
                .mapToLong(LoanAmount::getInterestAmount)
                .sum();
        Amounts amounts = amountsRepo.findByDate(new UsersPaidByMonth().DateFormatter());
        if(amounts == null){
            return 0L;
        }
        amounts.setTotalInterestAmount(totalInterestAmount);
        amountsRepo.save(amounts);
        return totalInterestAmount;
    }


//    public Long getTotalInterestAmount(LocalDateTime date){
//        Long amount = 0L;
//        List<LoanAmount> loanAmounts = loanAmountRepo.findAllByDate(date);
//        if (loanAmounts.isEmpty()){
//            return 0L;
//        }
//        for (LoanAmount loanAmount : loanAmounts) {
//            amount = loanAmount.getInterestAmount() + amount;
//        }
//        Amounts amounts = amountsRepo.findByDate(new CardPayment().DateFormatter());
//        if(amounts == null){
//            Amounts amounts1 = new Amounts();
//            amounts1.setDate(new UsersPaidByMonth().DateFormatter());
//            amounts1.setTotalInterestAmount(amount);
//            amounts1.setTotalamounts(amount);
//            amountsRepo.save(amounts1);
//        }
//        amounts.setTotalInterestAmount(amount);
//
//        amountsRepo.save(amounts);
//        return amount;
//    }

    public void Loans(LoanAmount  loanAmount){
         Loans loans = new Loans();
         loans.setId(loanAmount.getId());
         loans.setLoanAmount(loanAmount.getLoanAmount());
         loans.setUsers(loanAmount.getUsers());
         loans.setInterestAmount(loanAmount.getInterestAmount());
         loans.setLoanDate(loanAmount.getLoanDate());
         loansRepo.save(loans);
    }

    public List<LoanAmountDTO> getAllLoans(){
        Users retrivedusers = retriveLoggedInUser();
        List<Loans> loans = loansRepo.findAll();
        if(loans.isEmpty()){
            throw new LoanNotFound("No Loans Found");
        }
        return loans.stream()
            .map(loan -> new LoanAmountDTO(
                    loan.getId(),
                    loan.getLoanAmount(),
                    loan.getInterestAmount(),
                    loan.getLoanDate()))
                .collect(Collectors.toList());
    }

    public List<LoanAmountDTO> LoanAmountBTW(LocalDate start, LocalDate end) {
        Users retrivedusers = retriveLoggedInUser();
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
       List<LoanAmount> loanAmounts  = loanAmountRepo.findByDateBetween(startDateTime,endDateTime);
       if (loanAmounts.isEmpty()){
           throw new LoanNotFound(String.format("No Loans Found between %s and %s",start,end));
       }
        return loanAmounts.stream()
                .map(loan -> new LoanAmountDTO(
                        loan.getId(),
                        loan.getLoanAmount(),
                        loan.getInterestAmount(),
                        loan.getLoanDate()))
                .collect(Collectors.toList());
    }

    public Long LoanAmountInterestBTW(LocalDate start, LocalDate end) {
        Users retrivedusers = retriveLoggedInUser();
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        List<LoanAmount> loanAmounts  = loanAmountRepo.findByDateBetween(startDateTime,endDateTime);
        if(loanAmounts.isEmpty()){
            return 0L;
        }
        return loanAmounts.stream().mapToLong(LoanAmount::getInterestAmount).sum();
        }

    public Long LoanAmountInterestMonth() {
        Users retrivedusers = retriveLoggedInUser();
        LocalDate month =LocalDate.now();
        LocalDateTime Date = month.atStartOfDay();
        LocalDateTime endDateTime = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);
        List<LoanAmount> loanAmounts  = loanAmountRepo.findByDateBetween(Date,endDateTime);
        if (loanAmounts.isEmpty()){
            return 0L;
        }else {
        Long amount = loanAmounts.stream().mapToLong(LoanAmount::getInterestAmount).sum();
        String formattedDate = new Amounts().convertDate(String.valueOf(month));
        Amounts amounts = amountsRepo.findByDate(formattedDate);
         if(amounts == null){
            Amounts amounts1 = new Amounts();
             amounts1.setDate(new UsersPaidByMonth().DateFormatter());
            amounts1.setTotalInterestAmount(amount);
            amounts1.setTotalamounts(0L);
            amounts1.setCardsAmount(0L);
            amountsRepo.save(amounts1);
        }
        assert amounts != null;
        amounts.setTotalInterestAmount(amount);
        amountsRepo.save(amounts);
        return amount;
    }

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



//    public Long getTotalInterestAmount(LocalDateTime date) {
//        List<LoanAmount> loanAmounts = loanAmountRepo.findAllByDate(date);
//        Long totalInterestAmount = loanAmounts.stream()
//                .mapToLong(LoanAmount::getInterestAmount)
//                .sum();
//        return totalInterestAmount;
//    }
    }

