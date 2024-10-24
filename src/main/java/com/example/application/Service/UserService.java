package com.example.application.Service;

import com.example.application.DTO.AuthenticationResponce;
import com.example.application.DTO.LoginDTO;
import com.example.application.DTO.UserDTO;
import com.example.application.Entity.Users;
import com.example.application.Enums.Roles;
import com.example.application.Exceptions.UserNotFound;
import com.example.application.Repo.UserRepo;
import com.example.application.ServiceImp.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceImp {
    final
    JavaMailSender  javaMailSender;
    final
    JwtService  jwtService;
    final
    AuthenticationManager authenticationManager;

    private final UserRepo userRepo;
    private final MailService mailService;

    public UserService(UserRepo userRepo, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender javaMailSender, MailService mailService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
    }
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public List<UserDTO> AllUsers() {
        Users retrivedusers = retriveLoggedInUser();
       List<Users> users = userRepo.findAll();
        List<UserDTO> userDTO = users.stream().map(user -> new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getPhno()
                )).collect(Collectors.toList());
        return userDTO;
    }
    public ResponseEntity<?> addAdmin(Users users) {
        if(
                userRepo.findByUserNameOrPhno(users.getUserName(),users.getPhno()).isPresent())
        {
            return new ResponseEntity<>(String.format("UserName %s or %d already exist",users.getUserName(),users.getPhno()),HttpStatus.NOT_ACCEPTABLE);
        }
        users.setPassword(encoder.encode(users.getPassword()));
        users.setUserRole(Roles.ADMIN);
        userRepo.save(users);
        mailService.SendRegisterMail(users.getEmail(), users.getUserName());
        return  new ResponseEntity<>(new UserDTO(users.getUserId(), users.getUserName(), users.getPhno()),HttpStatus.OK);
    }

    public UserDTO addUser(Users users) {

        users.setPassword(encoder.encode(users.getPassword()));
        users.setUserRole(Roles.USER);
        userRepo.save(users);
        mailService.SendRegisterMail(users.getEmail(), users.getUserName());
        return  new  UserDTO(users.getUserId(), users.getUserName(), users.getPhno());
    }


    public ResponseEntity<String> DelUser(Integer userId) {
        Users retrivedusers = retriveLoggedInUser();
        Users users = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFound(String.format("UserId %d Doesn't Exist",userId))
        );
        userRepo.deleteById(userId);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    public ResponseEntity<?> userById(Integer userId) {
        Users retrivedusers = retriveLoggedInUser();
        Users users = userRepo.findById(userId).orElse(null);
        if(users == null) {
            return new ResponseEntity<>("Users Id Not Found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new UserDTO(users.getUserId(), users.getUserName(), users.getPhno()),HttpStatus.OK);
    }

    public ResponseEntity<?> UpdateUser(Users users, Integer userId) {
        Users retrivedusers = retriveLoggedInUser();
        Users users1 = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFound(String.format("UserId %d Doesn't Exist",userId)
                ));
        if(users.getUserName() == null && users.getPhno() == null){
            return  new ResponseEntity<>( "Enter Details", HttpStatus.NOT_ACCEPTABLE);
        }
       else if(users.getUserName() == null ){
            users.setUserName(users1.getUserName());
        }
       else if (users.getPhno() == null) {
            users.setPhno(users1.getPhno());
        }
        users1.setUserName(users.getUserName());
        users1.setPhno(users.getPhno());
        userRepo.save(users1);
       return new ResponseEntity<>(new UserDTO(users1.getUserId(),users1.getUserName(), users1.getPhno()),HttpStatus.OK);
    }

    public AuthenticationResponce Verify(LoginDTO loginDTO){

        Optional<Users> users = userRepo.findByPhno(loginDTO.getPhno());
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(users.get().getUserName(),loginDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            var Token = jwtService.generateToken(users.get().getUserName());
            AuthenticationResponce authenticationResponce = new AuthenticationResponce();
            authenticationResponce.setUserId(users.get().getUserId());
            authenticationResponce.setToken(Token);
            authenticationResponce.setRole(users.get().getUserRole());
            authenticationResponce.setUserName(users.get().getUserName());
            mailService.SendLoginMail(users.get().getEmail());
            return authenticationResponce;
        }
       return null;
    }
    private Users retriveLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
            throw new BadCredentialsException("Bad Credentials login ");
        String username = authentication.getName();
        System.out.println("In Logged In User "+username);
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
    }


    }
