package com.example.application.Repo;

import com.example.application.Entity.Otp;
import com.example.application.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OtpRepo extends JpaRepository<Otp, Integer> {
    Otp findByUsers(Users users);
    @Query("select b from Otp b where b.Otp=:otp and b.users=:user")
    Otp findByOtpAndUsers(String otp, Users user);
}
