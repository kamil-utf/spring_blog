package com.example.blog.exception;

public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException() {
        super();
    }

    public IllegalOperationException(String message) {
        super(message);
    }

}
