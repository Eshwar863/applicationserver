package com.example.application.DTO;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class UserDTO {

    private Integer userId;
    private String userName;
    private Long phno;

    public UserDTO(Integer userId, String userName, Long phno) {
        this.userId = userId;
        this.userName = userName;
        this.phno = phno;
    }
}
