package com.arjolpanci.restservice.util;

public class InsufficientAccessException extends Exception{

    private String message;

    public InsufficientAccessException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
