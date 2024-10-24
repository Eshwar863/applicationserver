package com.example.application.Service;

import com.example.application.DTO.ResetPasswordDTO;
import com.example.application.Entity.Otp;
import com.example.application.Entity.Users;
import com.example.application.Repo.OtpRepo;
import com.example.application.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service

public class MailService {
    final
    JavaMailSender mailSender;
     final JwtService jwtService;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private static final int OTP_LENGTH = 6;
     static final int OTP_EXPIRY_MINUTES = 5;
     final UserRepo userRepo;
    final
    OtpRepo otpRepo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public MailService(JavaMailSender mailSender, UserRepo userRepo, OtpRepo otpRepo, JwtService jwtService) {
        this.mailSender = mailSender;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> SendOtp(String email) {
        Users users = userRepo.findByEmail(email);
        if (users == null) {
            return new ResponseEntity<>("Mail Not Found", HttpStatus.NOT_FOUND);
        }
        Otp otpexist = otpRepo.findByUsers(users);
        if (otpexist!= null) {
            SimpleMailMessage message1 = new SimpleMailMessage();
            message1.setFrom(fromEmail);
            message1.setTo(email);
            message1.setSubject("Otp For Forgot Password");
            message1.setText(String.format("Hello! %s,\n" +
                    "Your otp for Forgot Password \n" +
                    "OTP : '%s' ,\n" +
                    "expires at %s",users.getUserName(), otpexist.getOtp(),otpexist.getValidTill()));
            mailSender.send(message1);

            return new ResponseEntity<>("Otp Already Sent Please try After Some Time", HttpStatus.OK);
        }

        String otp = generateOtp();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Otp For Forgot Password");
        message.setText(String.format("Hello! %s,\n"+
                "Your otp for Forgot Password is : '%s'\n"+
                "expires in 5mins.",users.getUserName(),otp));
        mailSender.send(message);
        otp(users,otp);
        return ResponseEntity.ok("Otp Sent to "+email);
    }

    public void otp(Users users, String otp) {
        Otp otpEntity = new Otp();
        otpEntity.setOtp(otp);
        otpEntity.setUsers(users);
        otpEntity.setValidTill(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
       otpRepo.save(otpEntity);
        }

        public ResponseEntity<?> ValidateOtp(String otp,Users users){
            Otp otp1 = otpRepo.findByOtpAndUsers(otp,users);
            if (otp1 == null) {
                return new ResponseEntity<>("Invalid Otp", HttpStatus.BAD_REQUEST);
            }
            if(otp1.getValidTill().isBefore(LocalDateTime.now())){
                otpRepo.delete(otp1);
                return new ResponseEntity<>("Otp Expired", HttpStatus.BAD_REQUEST);
            }
            if (otp1.getOtp().equals(otp)){
                otpRepo.delete(otp1);
                if (jwtService != null) {
                String token = jwtService.generateToken(users.getUserName());
                return ResponseEntity.ok(token);
                }
                else {
                    return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return null;
        }

    public String generateOtp() {
        Random random = new Random();
        int otpNumber = random.nextInt((int) Math.pow(10, OTP_LENGTH));
        return String.format("%0" + OTP_LENGTH + "d", otpNumber);
    }

    public ResponseEntity<?> forgotpassword(ResetPasswordDTO resetPasswordDTO) {
        Users users = retriveLoggedInUser();

        if (users == null) {
            return new ResponseEntity<>("Unable to process", HttpStatus.GATEWAY_TIMEOUT);
        }
        if(resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())){
            Users users1 = userRepo.findByUserName1(users.getUserName());
            users.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
            userRepo.save(users);
            return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);
        }

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

    public void SendLoginMail(String email) {
        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Login Detected");
            message.setText("New Login Detected\n" +
                    "Your account was recently accessed on " + getCurrentFormattedDateTime() + " in Telangana, India.\n" +
                    "\n" +
                    "If you recognize this activity, there is no further action required.\n" +
                    "\n" +
                    "If this was a mistake or done without your consent, you should Reset Your Password.");
            mailSender.send(message);
        }).start();
    }

    public void SendRegisterMail(String email,String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(String.format("Congratulations! %s Your Registration is Complete",username));
        message.setText(String.format(
                "Hello %s,\n\n" +
                "We are thrilled to inform you that your account registration was successfully completed on %s  in Telangana, India.\n\n" +
                "\n"+
                "Best regards,\n" +
                        "The Team",
                username, getCurrentFormattedDateTime()));
        mailSender.send(message);
    }
    public  String getCurrentFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return now.format(formatter);
    }

//        public static <T> void Generic(T t){
//
//
//        }


}

