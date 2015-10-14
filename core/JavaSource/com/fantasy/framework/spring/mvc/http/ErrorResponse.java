package com.fantasy.framework.spring.mvc.http;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private String message;
    private List<Error> fieldErrors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void addFieldError(String name, String message) {
        if (this.fieldErrors == null) {
            this.fieldErrors = new ArrayList<Error>();
        }
        this.fieldErrors.add(new Error(name, message));
    }

    public List<Error> getFieldErrors() {
        return fieldErrors;
    }

}