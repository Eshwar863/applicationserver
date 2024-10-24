package com.example.application.ServiceImp;

import com.example.application.DTO.UserDTO;

import java.util.List;

public interface UsersPaidByMonthServiceImp {

    Boolean MarkAsPaid(Integer userid, String monthYear);
    Boolean MarkAsUnPaid(Integer userid, String monthYear);
    List<UserDTO> getPaidUsersByMonth();
    List<UserDTO> getUnPaidUsersByMonth();
    Long PaidAmount(Long amount);

}
