package com.example.application.Repo;

import com.example.application.Entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CardPaymentRepo extends JpaRepository<CardPayment, Integer> {
    @Query("SELECT b FROM CardPayment b WHERE b.cards.cName = :CName AND b.date = :date")
    CardPayment findByCards_CNameAndDate(String CName, String date);
//    @Query("SELECT b.amount,b.date FROM CardPayment b where b.cards.cName = :CName")
//    List<CardPayment> findAmountByCName(String CName);
    @Query("SELECT b.amount, b.date FROM CardPayment b WHERE b.cards.cName = :CName")
    List<Object[]> findAmountByCName(String CName);
//    @Query("select b from CardPayment b where b.date<=:date")
    List<CardPayment> findAllByDate(String date);

}
