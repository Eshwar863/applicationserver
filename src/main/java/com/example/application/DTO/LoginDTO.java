package com.example.application.DTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LoginDTO {

    private Long phno;
    private String password;
}
