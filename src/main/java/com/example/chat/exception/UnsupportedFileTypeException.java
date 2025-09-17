package com.example.chat.exception;

public class UnsupportedFileTypeException extends RuntimeException{
    public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(Object fieldName) {
        super(String.format("Unsupported file type : '%s'", fieldName));
    }
}
