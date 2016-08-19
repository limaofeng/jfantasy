package org.jfantasy.framework.spring.validation;

public interface Validator<T> {

    void validate(T value) throws ValidationException;

}
