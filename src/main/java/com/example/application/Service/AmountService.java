package com.example.application.Service;

import com.example.application.Entity.Amounts;
import com.example.application.Entity.Users;
import com.example.application.Entity.UsersPaidByMonth;
import com.example.application.Repo.AmountsRepo;
import com.example.application.Repo.UserRepo;
import com.example.application.Repo.UsersPaidByMonthRepo;
import com.example.application.ServiceImp.AmountServiceImp;
import com.example.application.ServiceImp.UsersPaidByMonthServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AmountService implements AmountServiceImp {
    final
    AmountsRepo amountsRepo;
    final
    UserRepo userRepo;
    final
    UsersPaidByMonthRepo usersPaidByMonthRepo;

    public AmountService(AmountsRepo amountsRepo, UserRepo userRepo, UsersPaidByMonthRepo usersPaidByMonthRepo) {
        this.amountsRepo = amountsRepo;
        this.userRepo = userRepo;
        this.usersPaidByMonthRepo = usersPaidByMonthRepo;
    }


    public Amounts getByDate(){
        Amounts amounts = amountsRepo.findByDate(new Amounts().DateFormatter());
        if(amounts == null){
            Amounts newAmounts = new Amounts();
            newAmounts.setDate(new Amounts().DateFormatter());
            newAmounts.setTotalInterestAmount(0L);
            newAmounts.setUserPaidByAmount(0L);
            newAmounts.setCardsAmount(0L);
            newAmounts.setTotalamounts(newAmounts.getTotalInterestAmount()+ newAmounts.getCardsAmount()+newAmounts.getUserPaidByAmount());
            amountsRepo.save(newAmounts);
        }
        else {
            Long amount = amounts.getTotalInterestAmount() - amounts.getCardsAmount()+amounts.getUserPaidByAmount();
            if (amounts == null){
                amounts.setTotalamounts(0L);
            }

            amounts.setTotalamounts(amount);
        }
        return amounts;
    }

    public void update() {
        Amounts amounts = amountsRepo.findByDate(new Amounts().DateFormatter());
        if (amounts == null) {
            getByDate();
        }
        List<Users> users = userRepo.findAll();
        for(Users user : users){
            UsersPaidByMonth paidByMonth = usersPaidByMonthRepo.findByUsers_UserIdAndMonthYear(user.getUserId(),new Amounts().DateFormatter());
            if (paidByMonth == null) {
                UsersPaidByMonth usersPaidByMonth1 = new UsersPaidByMonth();
                usersPaidByMonth1.setUsers(user);
                usersPaidByMonth1.setPaid(false);
                usersPaidByMonth1.setMonthYear(usersPaidByMonth1.DateFormatter());
                usersPaidByMonthRepo.save(usersPaidByMonth1);
            } else {
                paidByMonth.setPaid(false);
                usersPaidByMonthRepo.save(paidByMonth);
            }
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
}
