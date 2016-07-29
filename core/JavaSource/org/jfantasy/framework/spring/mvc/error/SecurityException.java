package org.jfantasy.framework.spring.mvc.error;

import org.springframework.http.HttpStatus;

public class SecurityException extends RestException {

    private final static int prefix = HttpStatus.FORBIDDEN.value() * 100;

    private float code;

    public SecurityException(float code, String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
        this.code = prefix + code;
    }

    public SecurityException(String message) {
        super(message);
    }

    public float getCode() {
        return code;
    }

}
