package com.cbo.sso.exceptions;

public class NoSuchUserExistsException extends RuntimeException{
    private String message;

    public NoSuchUserExistsException() {}

    public NoSuchUserExistsException(String msg)
    {
        super(msg);
        this.message = msg;
    }
}
