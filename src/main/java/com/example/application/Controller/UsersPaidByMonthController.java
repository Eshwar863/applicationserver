package com.example.application.Controller;

import com.example.application.DTO.UserDTO;
import com.example.application.Entity.Amounts;
import com.example.application.Entity.Users;
import com.example.application.Service.UsersPaidByMonthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("api")
public class UsersPaidByMonthController {

    private final UsersPaidByMonthService usersPaidByMonthService;


    public UsersPaidByMonthController(UsersPaidByMonthService usersPaidByMonthService ) {
        this.usersPaidByMonthService = usersPaidByMonthService;

    }
    @GetMapping("paid")
    public ResponseEntity<?> getPaid() {
         List<UserDTO> userDTO  = usersPaidByMonthService.getPaidUsersByMonth();
         if(userDTO.isEmpty()){
             return new ResponseEntity<>("No Paid Users Found", HttpStatus.NOT_FOUND);
         }
         return ResponseEntity.ok(userDTO);
    }
    @GetMapping("unpaid")
    public ResponseEntity<?> getUnPaid() {
        List<UserDTO> userDTO  = usersPaidByMonthService.getUnPaidUsersByMonth();
        if(userDTO.isEmpty()){
            return new ResponseEntity<>("No Paid Users Found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userDTO);
    }
    @PutMapping("{userid}/paid/{MonthYear}")
    public ResponseEntity<?> paid(@PathVariable(name = "userid") Integer userid,
                        @PathVariable(name = "MonthYear") String MonthYear) {
        if(userid == null || MonthYear == null) {
            return new ResponseEntity<>("Userid and MonthYear are required", HttpStatus.BAD_REQUEST);
        }
            Boolean Type = usersPaidByMonthService.MarkAsPaid(userid,MonthYear);
        if(Type == null) {
            return new ResponseEntity<>("Failed to Update UserPaid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Type,HttpStatus.OK);
    }

    @PutMapping("{userid}/unpaid/{MonthYear}")
    public ResponseEntity<?> Unpaid(@PathVariable(name = "userid") Integer userid,
                                  @PathVariable(name = "MonthYear") String MonthYear) {
        if(userid == null || MonthYear == null) {
            return new ResponseEntity<>("Userid and MonthYear are required", HttpStatus.BAD_REQUEST);
        }
        Boolean Type = usersPaidByMonthService.MarkAsUnPaid(userid,MonthYear);
        if(Type == null) {
            return new ResponseEntity<>("Failed to Update UserPaid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Type,HttpStatus.OK);
    }

    @GetMapping("userPaidAmount/{amount}")
    public ResponseEntity<?> getUserPaidAmount(@PathVariable(name = "amount") Long amount) {
        if(amount == null || amount <= 0) {
            return new ResponseEntity<>("Amount is required", HttpStatus.BAD_REQUEST);
        }
        Long Paidamount =  usersPaidByMonthService.PaidAmount(amount);
        if(Paidamount == null) {
            return new ResponseEntity<>("Unable to Update Paid Amount", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Paidamount,HttpStatus.OK);
    }

}