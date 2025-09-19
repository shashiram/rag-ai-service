package com.rag.chat.util;

import com.rag.chat.constant.CustomErrorCode;
import com.rag.chat.exception.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ExceptionUtils {
    public static SessionNotFoundException sessionNotFound(UUID sessionId) {
        return new SessionNotFoundException(CustomErrorCode.SESSION_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "Session with ID: " + sessionId);
    }

    public static OpenAIClientApiError openAIClientApiCallError() {
        return new OpenAIClientApiError(CustomErrorCode.EXTERNAL_SERVICE_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Open AI api error ");
    }

    public static UnsupportedFileTypeException unsupportedFileTypeException(String fileName) {
        return new UnsupportedFileTypeException(CustomErrorCode.INVALID_FILE_FORMAT,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported file type: " + fileName);
    }

    public static VectorStoreDBException vectorStoreViolationException() {
        return new VectorStoreDBException(CustomErrorCode.VECTOR_STORE_DB_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "VectorStore error: ");
    }

    public static DataBaseException dataBaseException() {
        return new DataBaseException(CustomErrorCode.DATABASE_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Rag Chat DB  error: ");
    }

    public static UserSessionNotFoundException userSessionNotFoundException() {
        return new UserSessionNotFoundException(CustomErrorCode.USER_SESSION_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "User Session not found  error: ");
    }
}
