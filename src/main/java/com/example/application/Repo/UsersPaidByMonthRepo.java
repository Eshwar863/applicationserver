package com.example.application.Repo;

import com.example.application.Entity.Users;
import com.example.application.Entity.UsersPaidByMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersPaidByMonthRepo extends JpaRepository<UsersPaidByMonth,Integer> {
    //Optional<UsersPaidByMonth> findByUsersOrMonthYear(Users users, String monthYear);
//    @Query
//    List<UsersPaidByMonth> findByMonthYear(String monthYear);
    @Query("SELECT b.users FROM UsersPaidByMonth b WHERE b.monthYear = :monthYear AND b.paid = true")
    List<Users> findByMonthYearAndPaid(String monthYear);
    @Query("SELECT b.users FROM UsersPaidByMonth b WHERE b.monthYear = :monthYear AND b.paid = false ")
    List<Users> findByMonthYearAndUnPaid(String monthYear);
    UsersPaidByMonth findByUsers_UserIdAndMonthYear(Integer userid, String monthYear);
}
