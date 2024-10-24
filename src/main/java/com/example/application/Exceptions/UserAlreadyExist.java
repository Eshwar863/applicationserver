package com.example.application.Exceptions;

public class UserAlreadyExist extends RuntimeException {

  private String message;
  public UserAlreadyExist(String message){
    super(message);
    this.message= message;
  }
}
