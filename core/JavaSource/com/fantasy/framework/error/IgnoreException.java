package com.fantasy.framework.error;

public class IgnoreException extends RuntimeException {

    public IgnoreException(String message) {
        super(message);
    }

    public IgnoreException(String message, Exception e) {
        super(message, e);
    }

    public IgnoreException(Exception e) {
        super(e);
    }

}
