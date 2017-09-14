package com.java.group11.exception;

public class   NetworkException extends Exception {

    public NetworkException() {
        super();
    }

    public NetworkException(Throwable throwable) {
        super(throwable);
    }

    public NetworkException(String message) {
        super(message);
    }
}
