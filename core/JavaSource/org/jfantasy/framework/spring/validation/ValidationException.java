package org.jfantasy.framework.spring.validation;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 3828944393587353554L;

    public ValidationException(String message) {
        super(message);
    }

}