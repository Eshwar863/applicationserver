package com.example.application.Controller;

import com.example.application.DTO.UserDTO;
import com.example.application.Entity.Users;
import com.example.application.Repo.UserRepo;
import com.example.application.Service.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class UserController {

    final
    UserService userService;
    private final UserRepo userRepo;

    public UserController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping("allusers")
    public ResponseEntity<?> user (){
        List<UserDTO> userDTO  = userService.AllUsers();
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("{userId}/getuser")
    public ResponseEntity<?> userById (@PathVariable (name = "userId") Integer userId){
        if (userId == null) {
            return new ResponseEntity<>("Enter UserId",HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> userDTO = userService.userById(userId);
        if (userDTO == null) {
            return new ResponseEntity<>("User Doesn't Exist",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("{userId}/deluser")
    public ResponseEntity<?> delUser(@PathVariable (name = "userId") Integer userId){
        return new ResponseEntity<>(userService.DelUser(userId), HttpStatus.OK);
    }
    @PostMapping("{userid}/{password}")
    public ResponseEntity<String> Validatepassword(@PathVariable(name = "userId") Integer userId,@PathVariable(name = "password") String password){
        return userService.validatepassword(userId,password);
    }
    @PutMapping("{userId}/updateuser")
    public ResponseEntity<?> UpdateUser(@RequestBody Users users, @PathVariable (name = "userId") Integer userId){
        ResponseEntity<?> usersdto = userService.UpdateUser(users,userId);
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersdto,HttpStatus.OK);

    }

    @PostMapping("adduser")
    public ResponseEntity<?> addUser(@RequestBody Users users){
        if (users == null||users.getUserName() == null||users.getPassword() == null||users.getPhno() == null||users.getEmail() == null) {
            return new ResponseEntity<>("Enter Valid Details",HttpStatus.NOT_ACCEPTABLE);
        }
        if(userRepo.findByUserNameOrPhno(users.getUserName(),users.getPhno()).isPresent())
        {
           return new ResponseEntity<>(String.format("UserName %s or %d already exist",users.getUserName(),users.getPhno()),HttpStatus.CONFLICT);
        }
        UserDTO userDTO = userService.addUser(users);
        if (userDTO == null) {
            return new ResponseEntity<>("Failed to Create User",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PostMapping("addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody Users users){
        if (users == null||users.getUserName() == null||users.getPassword() == null||users.getPhno() == null||users.getEmail() == null) {
            return new ResponseEntity<>("Enter Valid Details",HttpStatus.NOT_ACCEPTABLE);
        }
        if(userRepo.findByUserNameOrPhno(users.getUserName(),users.getPhno()).isPresent())
        {
            return new ResponseEntity<>(String.format("UserName %s or %d already exist",users.getUserName(),users.getPhno()),HttpStatus.CONFLICT);
        }
        ResponseEntity<?> userDTO = userService.addAdmin(users);
        if (userDTO == null) {
            return new ResponseEntity<>("Failed to Create Admin",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

}
