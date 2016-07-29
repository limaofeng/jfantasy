package org.jfantasy.framework.spring.mvc.http;

import java.util.ArrayList;
import java.util.List;

class ErrorResponse {

    private float code;
    private String message;

    private List<Error> errors;

    ErrorResponse(){
    }

    ErrorResponse(float code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    void addError(String name, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new Error(name, message));
    }

    public float getCode() {
        return code;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setCode(float code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}