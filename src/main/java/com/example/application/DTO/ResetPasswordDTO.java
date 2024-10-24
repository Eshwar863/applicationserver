package com.example.application.DTO;

import lombok.Data;

@Data
public class ResetPasswordDTO {

    private String password;
    private String confirmPassword;
}
