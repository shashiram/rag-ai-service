package com.rag.chat.exception;

import com.rag.chat.constant.CustomErrorCode;
import org.springframework.http.HttpStatus;

public class UserSessionNotFoundException extends ApiException {

    public UserSessionNotFoundException(CustomErrorCode errorCode, HttpStatus httpStatus, String details) {
        super(errorCode, httpStatus, details);
    }
}
