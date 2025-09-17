package com.example.chat.exception;

public class ChatClientException extends RuntimeException{
    public ChatClientException(String message) {
        super(message);
    }

    public ChatClientException(Object fieldName) {
        super(String.format("session not found with : '%s'", fieldName));
    }
}
