package com.rag.chat.constant;

public enum CustomErrorCode {

    // Resource Errors (1000-1099)
    SESSION_NOT_FOUND(1000, "Chat session not found", "RES_001"),
    USER_SESSION_NOT_FOUND(1001, "Chat session not found", "RES_002"),
    // DB Errors (1100-1199)
    DATABASE_ERROR(1100, "Database error", "DB_001"),
    VECTOR_STORE_DB_ERROR(1101, "Database constraint violation", "DB_002"),

    // External Service Errors (1200-1299)
    EXTERNAL_SERVICE_ERROR(1200, "External service error", "EXT_001"),

    // File & Storage Errors (1300-1399)
    INVALID_FILE_FORMAT(1300, "Invalid file format", "FILE_001"),
    // System Errors (1600-1699)
    INTERNAL_SERVER_ERROR(1400, "Internal server error", "SYS_001");

    private final int code;
    private final String message;
    private final String errorCode;

    CustomErrorCode(int code, String message, String errorCode) {
        this.code = code;
        this.message = message;
        this.errorCode = errorCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static CustomErrorCode fromErrorCode(String errorCode) {
        for (CustomErrorCode ec : values()) {
            if (ec.errorCode.equals(errorCode)) {
                return ec;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
    }
