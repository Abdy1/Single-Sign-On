package com.cbo.sso.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    private String message;

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
