package com.example.application.Controller;

import com.example.application.DTO.ResetPasswordDTO;
import com.example.application.Entity.Otp;
import com.example.application.Entity.Users;
import com.example.application.Repo.UserRepo;
import com.example.application.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    MailService mailService;
    @Autowired
    UserRepo userRepo;

    @PostMapping("verify/{email}")
    public ResponseEntity<?> verify(@PathVariable String email) {
     return mailService.SendOtp(email);
    }

    @PostMapping("valid/{otp}/{username}")
    public ResponseEntity<?> Validate(@PathVariable String otp,@PathVariable String username) {
        Users users = userRepo.findByUserName1(username);
        if(users == null) {
            return new ResponseEntity<>("UserNotFound", HttpStatus.BAD_GATEWAY);
        }
       return mailService.ValidateOtp(otp,users);
    }

    @PostMapping("resetpassword")
    public ResponseEntity<?> resetpassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        System.out.println(resetPasswordDTO + "controller ");
        if(resetPasswordDTO.getPassword() == null || resetPasswordDTO.getConfirmPassword() == null || resetPasswordDTO == null)
        {
        return new ResponseEntity<>("Enter Password",HttpStatus.BAD_REQUEST);
        }
        return mailService.forgotpassword(resetPasswordDTO);
    }

}
