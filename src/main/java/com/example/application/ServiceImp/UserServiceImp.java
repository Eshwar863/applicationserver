package com.example.application.ServiceImp;

import com.example.application.DTO.AuthenticationResponce;
import com.example.application.DTO.LoginDTO;
import com.example.application.DTO.UserDTO;
import com.example.application.Entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceImp {

    List<UserDTO> AllUsers();
    UserDTO addUser(Users users);
    ResponseEntity<String> DelUser(Integer userId);
    ResponseEntity<?> userById(Integer userId);
    ResponseEntity<?> UpdateUser(Users users, Integer userId);
    ResponseEntity<?> addAdmin(Users users);
    AuthenticationResponce Verify(LoginDTO loginDTO);
}
