package com.rag.chat.exception;

import com.rag.chat.constant.CustomErrorCode;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private final CustomErrorCode customErrorCode;
    private final HttpStatus httpStatus;
    private final String details;

    public ApiException(CustomErrorCode customErrorCode, HttpStatus httpStatus, String details) {
        this.customErrorCode = customErrorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public CustomErrorCode getCustomErrorCode() {
        return customErrorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDetails() {
        return details;
    }
}
