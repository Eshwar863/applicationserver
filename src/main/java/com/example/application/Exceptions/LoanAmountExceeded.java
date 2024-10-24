package com.example.application.Exceptions;

public class LoanAmountExceeded extends RuntimeException {
    private String message;
    public LoanAmountExceeded(String message){
        super(message);
        this.message= message;
    }}