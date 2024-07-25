package com.zequence.ZequenceIms.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("Invalid user ID provided"); // Default message
    }
    public InvalidIdException(String message) {
        super(message); // Allow custom message
    }

}