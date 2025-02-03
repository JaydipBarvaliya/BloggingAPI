package com.blogging.exception;

public class BlogSlugAlreadyExistsException extends RuntimeException {

    // Constructor that accepts a custom message
    public BlogSlugAlreadyExistsException(String message) {
        super(message);
    }
}
