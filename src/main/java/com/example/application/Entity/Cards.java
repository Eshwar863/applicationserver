package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Getter
@Setter
public class Cards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Cid;
    @Column(nullable = false)
    private String cName;
    @Column(nullable = false)
    private String cType;
    @Column(nullable = false)
    private java.sql.Date cStartingDate;
    @Column(nullable = false)
    private java.sql.Date cExpireDate;

}
