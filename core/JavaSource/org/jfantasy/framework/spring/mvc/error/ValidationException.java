package org.jfantasy.framework.spring.mvc.error;

import org.springframework.http.HttpStatus;

public class ValidationException extends RestException{

    private final static float prefix = HttpStatus.UNPROCESSABLE_ENTITY.value() * 100f;

    private float code;

    public ValidationException(float code, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
        this.code = prefix + code;
    }

    public ValidationException(String message) {
        super(message);
    }

    public float getCode() {
        return code;
    }

}
