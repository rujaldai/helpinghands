package com.helpinghands.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resource, String identifier) {
        super(resource + " with " + identifier + " already exists");
    }
}

