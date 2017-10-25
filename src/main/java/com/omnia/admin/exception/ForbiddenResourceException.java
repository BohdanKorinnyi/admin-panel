package com.omnia.admin.exception;

public class ForbiddenResourceException extends RuntimeException {
    public ForbiddenResourceException() {
        super("You don't have an access to this resource");
    }
}
