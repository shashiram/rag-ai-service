package com.rag.chat.exception;

import com.rag.chat.constant.CustomErrorCode;
import org.springframework.http.HttpStatus;

public class OpenAIClientApiException extends ApiException {
    public OpenAIClientApiException(CustomErrorCode errorCode, HttpStatus httpStatus, String details) {
        super(errorCode, httpStatus, details);
    }
}
