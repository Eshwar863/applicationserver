package com.example.application.DTO;

import com.example.application.Enums.Roles;
import lombok.Data;

import javax.management.relation.Role;
@Data
public class AuthenticationResponce {
    private String token;
    private Integer userId;
    private String UserName;
    private Roles role;
}
