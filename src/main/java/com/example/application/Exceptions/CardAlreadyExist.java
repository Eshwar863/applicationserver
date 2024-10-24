package com.example.application.Exceptions;

public class CardAlreadyExist extends RuntimeException {
    private String message;
    public CardAlreadyExist(String message){
        super(message);
        this.message= message;
    }
}
