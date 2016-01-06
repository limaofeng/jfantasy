package org.jfantasy.framework.util.validator;

import org.jfantasy.framework.util.validator.exception.ValidationException;

public abstract interface Validator {
    public abstract void validate(Object paramObject) throws ValidationException;
}