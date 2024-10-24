package com.example.application.Exceptions;

public class UserWithLoanNotExist extends RuntimeException {
    private String message;
    public UserWithLoanNotExist(String message){
        super(message);
        this.message= message;
    }}