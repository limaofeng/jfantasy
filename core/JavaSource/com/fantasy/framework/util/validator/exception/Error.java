package com.fantasy.framework.util.validator.exception;

import com.fantasy.framework.util.common.StringUtil;

public class Error {
    private String field;
    private String message;

    public Error(String message) {
	this.message = message;
    }

    public Error(String field, String message) {
	this.field = field;
	this.message = message;
    }

    public boolean isStack() {
	return StringUtil.isNotBlank(this.field);
    }

    public String getField() {
	return this.field;
    }

    public String getMessage() {
	return this.message;
    }
}