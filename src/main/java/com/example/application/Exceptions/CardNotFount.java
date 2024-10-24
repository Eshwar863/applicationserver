package com.example.application.Exceptions;

public class CardNotFount extends RuntimeException {
    private String message;
    public CardNotFount(String message){
        super(message);
        this.message= message;
    }
}
