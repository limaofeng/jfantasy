package com.fantasy.framework.util.validator;

import com.fantasy.framework.util.validator.entities.FieldValidator;

import java.util.List;
import java.util.Map;

public abstract interface Validateable {
    public abstract Map<String, List<String>> validate(String paramString, Map<String, ?> paramMap);

    public abstract Validator getValidator(String paramString);

    public abstract void addValidator(String paramString, Validator paramValidator);

    public abstract void addFieldValidators(String paramString1, String paramString2, List<FieldValidator> paramList);
}