package com.nagarro.miniassignment.utilities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorUtils {

    public static Map<String, Object> createErrorResponse(String message, int code) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("errorCode", code);
        errorResponse.put("timestamp", LocalDateTime.now());
        return errorResponse;
    }
}
