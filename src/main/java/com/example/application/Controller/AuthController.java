package com.example.application.Controller;

import com.example.application.DTO.AuthenticationResponce;
import com.example.application.DTO.LoginDTO;
import com.example.application.Entity.Users;
import com.example.application.Repo.UserRepo;
import com.example.application.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/auth")
public class AuthController {
final
UserService userService;
    private final UserRepo userRepo;

    public AuthController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostMapping("user/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        if (loginDTO.getPhno() == null || loginDTO.getPassword() == null) {
            return new ResponseEntity<>("Phno or Password can't be empty", HttpStatus.NOT_ACCEPTABLE);
        }
        AuthenticationResponce authenticationResponce = userService.Verify(loginDTO);
        if (authenticationResponce == null){
            return new ResponseEntity<>("Login Failed",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authenticationResponce, HttpStatus.OK);
    }

    private Users retriveLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null || authentication.isAuthenticated()){
            throw new BadCredentialsException("Bad credentials");
        }
        String username = authentication.getName();
        return userRepo.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }

    @GetMapping("Allusers")
    public List<Users> AllUsers(){
        return userRepo.findAll();
    }
}
