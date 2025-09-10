package com.equipo01.featureflag.featureflag.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/**
 * This enum is used to ensure consistent error handling and messaging throughout the application.
 */
@Getter
@RequiredArgsConstructor
public enum MessageError {
    //USER ERRORS
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "The email is already in use."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", "The username is already in use."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "INVALID_USERNAME", "The username is invalid."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "The password is invalid."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "The user was not found."),

    //FEATURE ERRORS
    FEATURE_NOT_FOUND(HttpStatus.NOT_FOUND, "FEATURE_NOT_FOUND", "The feature was not found."),
    FEATURE_ALREADY_EXISTS(HttpStatus.CONFLICT, "FEATURE_ALREADY_EXISTS", "The feature name already exists."),

    //SPRING SECURITY ERRORS
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "JWT token is missing or invalid."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "You don't have permission to access this resource."),

    //OTHERS ERRORS
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", ""),
    VALIDATION_PARAMETER_NOT_VALID(HttpStatus.BAD_REQUEST, "VALIDATION_PARAMETER_NOT_VALID", ""),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", ""),
    DTO_FIELDS_NOT_VALID(HttpStatus.BAD_REQUEST, "DTO_FIELDS_NOT_VALID", ""),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATA_ACCESS_ERROR", ""),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "");
    
    private final HttpStatus status;
    private final String message;
    private final String description;

  
}
