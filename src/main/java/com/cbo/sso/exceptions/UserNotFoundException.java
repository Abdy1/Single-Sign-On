package com.cbo.sso.exceptions;

public class UserNotFoundException extends RuntimeException{
    public  UserNotFoundException(String message){
        super(message);
    }
}
