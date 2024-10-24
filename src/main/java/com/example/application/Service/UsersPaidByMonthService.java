package com.example.application.Service;

import com.example.application.DTO.UserDTO;
import com.example.application.Entity.Amounts;
import com.example.application.Entity.CardPayment;
import com.example.application.Entity.Users;
import com.example.application.Entity.UsersPaidByMonth;
import com.example.application.Exceptions.UserNotFound;
import com.example.application.Repo.AmountsRepo;
import com.example.application.Repo.UserRepo;
import com.example.application.Repo.UsersPaidByMonthRepo;
import com.example.application.ServiceImp.UsersPaidByMonthServiceImp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsersPaidByMonthService implements UsersPaidByMonthServiceImp {
    final
    UsersPaidByMonthRepo usersPaidByMonthRepo;
    final
    UserRepo userRepo;
    final
    AmountsRepo amountsRepo;

    public UsersPaidByMonthService(UsersPaidByMonthRepo usersPaidByMonthRepo, UserRepo userRepo, AmountsRepo amountsRepo) {
        this.usersPaidByMonthRepo = usersPaidByMonthRepo;
        this.userRepo = userRepo;
        this.amountsRepo = amountsRepo;
    }


    public Boolean MarkAsPaid(Integer userid, String monthYear) {
        Users retrivedusers = retriveLoggedInUser();
        Users users = userRepo.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("UserId %d Doesn't Exist", userid)));
        UsersPaidByMonth usersPaidByMonth = usersPaidByMonthRepo.findByUsers_UserIdAndMonthYear(userid, monthYear);
        if (usersPaidByMonth == null) {
            UsersPaidByMonth usersPaidByMonth1 = new UsersPaidByMonth();
            usersPaidByMonth1.setUsers(users);
            usersPaidByMonth1.setPaid(true);
            usersPaidByMonth1.setMonthYear(usersPaidByMonth1.DateFormatter());
            usersPaidByMonthRepo.save(usersPaidByMonth1);
            return usersPaidByMonth1.getPaid();
        } else {
            usersPaidByMonth.setPaid(true);
            usersPaidByMonthRepo.save(usersPaidByMonth);
            return usersPaidByMonth.getPaid();
        }
    }

    public Boolean MarkAsUnPaid(Integer userid, String monthYear) {
        Users retrivedusers = retriveLoggedInUser();
        Users users = userRepo.findById(userid).orElseThrow(
                () -> new UserNotFound(String.format("UserId %d Doesn't Exist", userid)));
        UsersPaidByMonth usersPaidByMonth = usersPaidByMonthRepo.findByUsers_UserIdAndMonthYear(userid, monthYear);
        if (usersPaidByMonth == null) {
            UsersPaidByMonth usersPaidByMonth1 = new UsersPaidByMonth();
            usersPaidByMonth1.setUsers(users);
            usersPaidByMonth1.setPaid(false);
            usersPaidByMonth1.setMonthYear(usersPaidByMonth1.DateFormatter());
            usersPaidByMonthRepo.save(usersPaidByMonth1);
            return usersPaidByMonth1.getPaid();
        } else {
            usersPaidByMonth.setPaid(false);
            usersPaidByMonthRepo.save(usersPaidByMonth);
            return usersPaidByMonth.getPaid();
        }
    }

    public List<UserDTO> getPaidUsersByMonth() {
        Users retrivedusers = retriveLoggedInUser();
        List<Users> usersList = usersPaidByMonthRepo.findByMonthYearAndPaid(new Amounts().DateFormatter());
        List<UserDTO> userDTOList = usersList.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUserName(), user.getPhno()))
                .collect(Collectors.toList());

        return userDTOList;
    }


    public List<UserDTO> getUnPaidUsersByMonth() {
        Users retrivedusers = retriveLoggedInUser();
        List<Users> usersList = usersPaidByMonthRepo.findByMonthYearAndUnPaid(new Amounts().DateFormatter());
        List<UserDTO> userDTOList = usersList.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUserName(), user.getPhno()))
                .collect(Collectors.toList());

        return userDTOList;
    }

    public Long PaidAmount(Long amount) {
        Users retrivedusers = retriveLoggedInUser();
        UsersPaidByMonth paidByMonth = new UsersPaidByMonth();
        Long userPaid = (long) usersPaidByMonthRepo.findByMonthYearAndPaid(paidByMonth.DateFormatter()).size();
        Amounts amounts = amountsRepo.findByDate(new CardPayment().DateFormatter());
        if(userPaid == 0){
            amounts.setDate(new UsersPaidByMonth().DateFormatter());
            amounts.setUserPaidByAmount(0L);
            amountsRepo.save(amounts);
            return 0L;
        }

        Long UPAmount = userPaid* amount;
        if(amounts == null){
           Amounts amounts1 = new Amounts();
           amounts1.setDate(new UsersPaidByMonth().DateFormatter());
           amounts1.setUserPaidByAmount(UPAmount);
           amounts1.setTotalamounts(UPAmount);
           amountsRepo.save(amounts1);
        }
        amounts.setDate(new UsersPaidByMonth().DateFormatter());
        amounts.setUserPaidByAmount(UPAmount);
        amountsRepo.save(amounts);
        return UPAmount;
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

