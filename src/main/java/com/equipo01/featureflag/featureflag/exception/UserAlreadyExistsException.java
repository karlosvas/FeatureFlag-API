package com.equipo01.featureflag.featureflag.exception;

/**
 * Exception thrown when attempting to create a user that already exists in the
 * system.
 * 
 * This exception is a specialized runtime exception that indicates a business
 * logic
 * violation where a user registration or creation operation fails due to the
 * existence
 * of another user with the same identifying characteristics (e.g., username,
 * email).
 * 
 * The exception extends RuntimeException to provide unchecked exception
 * behavior,
 * allowing it to be thrown without explicit declaration in method signatures
 * while
 * still providing meaningful error information for exception handling
 * mechanisms.
 * 
 * Typical usage scenarios:
 * - User registration with duplicate username
 * - User creation with duplicate email address
 * - Administrative user creation conflicts
 * 
 * This exception should be caught by appropriate exception handlers to provide
 * user-friendly error messages and proper HTTP status codes (e.g., 409
 * Conflict).
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new UserAlreadyExistsException with the specified detail
     * message.
     * 
     * @param message The detail message explaining the reason for the exception.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
