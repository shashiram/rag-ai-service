package com.example.chat.exception;

public class SessionNotFoundException extends RuntimeException{
    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(Object fieldName) {
        super(String.format("session not found with : '%s'", fieldName));
    }
}
