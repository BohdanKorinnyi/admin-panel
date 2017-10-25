package com.omnia.admin.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Wrong username or password");
    }
}
