package com.blogging.exception;

public class UserNotFoundException extends RuntimeException {

    // Constructor with message
    public UserNotFoundException(String message) {
        super(message);  // Pass the message to the RuntimeException constructor
    }

    // Constructor with message and cause (for better error tracking)
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
