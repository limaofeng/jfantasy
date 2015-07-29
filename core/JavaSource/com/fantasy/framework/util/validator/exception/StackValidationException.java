package com.fantasy.framework.util.validator.exception;

import java.util.ArrayList;
import java.util.List;

public class StackValidationException extends ValidationException {

    private static final long serialVersionUID = 2298535691065890585L;

    protected List<Error> errors = new ArrayList<Error>();

    public StackValidationException() {
        super(StackValidationException.class.getName());
    }

    public StackValidationException(String message) {
        super(message);
    }

    public void addError(Error error) {
        this.errors.add(error);
    }

    public Error[] getStack() {
        return (Error[]) this.errors.toArray(new Error[this.errors.size()]);
    }
}