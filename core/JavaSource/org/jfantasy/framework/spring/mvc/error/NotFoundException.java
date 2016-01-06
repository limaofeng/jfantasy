package org.jfantasy.framework.spring.mvc.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }

}
