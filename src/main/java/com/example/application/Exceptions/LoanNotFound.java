package com.example.application.Exceptions;

public class LoanNotFound extends RuntimeException {
    private String message;
    public LoanNotFound(String message){
        super(message);
        this.message= message;
    }}
