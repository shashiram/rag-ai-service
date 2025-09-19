package com.rag.chat.exception;

import com.rag.chat.dto.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OpenAIClientApiError.class)
    public ResponseEntity<ApiErrorResponse> handleOpenAIClientApiErrorException(OpenAIClientApiError ex,WebRequest request) {
        return handleApiException(ex,request);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleSessionNotFoundException(SessionNotFoundException ex, WebRequest request) {
        logger.error("Session not found with ID: {}", ex.getMessage());
        return handleApiException(ex, request);
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<String> handleUnsupportedFileTypeException(UnsupportedFileTypeException ex) {
        logger.error("Unsupported file type: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(VectorStoreDBException.class)
    public ResponseEntity<ApiErrorResponse> handleVectorStoreException(VectorStoreDBException ex, WebRequest request) {
        logger.error("An error occurred on saving data in vector store : {}", ex.getMessage());
        return handleApiException(ex,request);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex, WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ex.getHttpStatus().value(),
                ex.getHttpStatus().getReasonPhrase(),
                ex.getCustomErrorCode().getErrorCode(),
                ex.getCustomErrorCode().getMessage(),
                path,
                ex.getDetails()
        );
        logger.warn("API Exception: {} - {}", ex.getCustomErrorCode().getErrorCode(), ex.getMessage(), ex);
        return new ResponseEntity<>(apiErrorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        logger.error("An unexpected error occurred {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
