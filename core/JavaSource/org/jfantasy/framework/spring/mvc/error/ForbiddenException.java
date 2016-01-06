package org.jfantasy.framework.spring.mvc.error;

import org.springframework.http.HttpStatus;

public class ForbiddenException  extends RestException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}
