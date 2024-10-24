package com.example.application.Repo;

import com.example.application.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByUserNameOrPhno(String userName, Long phno);
    @Query("select a from Users a where a.userName=:userName")
    Optional<Users> findByUserName(String userName);
    Optional<Users> findByPhno(Long phno);
    @Query("select b from Users b where b.email=:email")
    Users findByEmail(String email);
    @Query("select b from Users b where b.userName=:userName")
    Users findByUserName1(String userName);
}
