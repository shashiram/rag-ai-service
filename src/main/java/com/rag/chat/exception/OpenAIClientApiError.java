package com.rag.chat.exception;

import com.rag.chat.constant.CustomErrorCode;
import org.springframework.http.HttpStatus;

public class OpenAIClientApiError extends ApiException {
    public OpenAIClientApiError(CustomErrorCode errorCode, HttpStatus httpStatus, String details) {
        super(errorCode, httpStatus, details);
    }
}
