package com.cbo.sso.exceptions;

public class IncorrectUsernameOrPasswordException extends RuntimeException{
    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }
}
