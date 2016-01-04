package org.jfantasy.framework.util.validator.exception;

import org.jfantasy.framework.util.jackson.JSON;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 3828944393587353554L;

    public ValidationException(String message) {
        super(message);
    }

    public String toString() {
        if (this instanceof StackValidationException) {
            StackValidationException stack = (StackValidationException) this;
            return "ValidationException [message:" + getMessage() + ",errors:" + JSON.serialize(stack.errors) + "]";
        }
        return "ValidationException [" + JSON.serialize(this) + "]";
    }
}