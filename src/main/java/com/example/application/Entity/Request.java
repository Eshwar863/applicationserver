package com.example.application.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class Request {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "users_user_id")
    private Users users;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String Description;
    private LocalDateTime Date;
}
