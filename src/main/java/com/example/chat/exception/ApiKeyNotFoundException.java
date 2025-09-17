package com.example.chat.exception;

public class ApiKeyNotFoundException extends RuntimeException{
    public ApiKeyNotFoundException(String message) {
        super(message);
    }

    public ApiKeyNotFoundException(Object fieldName) {
        super(String.format("Api-Key not found with : '%s'", fieldName));
    }
}
