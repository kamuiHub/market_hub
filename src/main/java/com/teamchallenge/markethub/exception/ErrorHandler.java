package com.teamchallenge.markethub.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public final class ErrorHandler {

    public static Map<String, Object> invalidUniqueEmail() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.CONFLICT.value());
        responseBody.put("message", "A user with this email address already exists");
        return responseBody;
    }

    public static Map<String, Object> invalidUniquePhone() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.CONFLICT.value());
        responseBody.put("message", "A user with this phone number already exists");
        return responseBody;
    }

    public static Map<String, Object> invalidLoginPassword() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("message", "Password invalid");
        return responseBody;
    }

    public static Map<String, Object> invalidLoginEmail() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("message", "Email invalid");
        return responseBody;
    }
}
