package com.fantasy.framework.util.validator;

public abstract interface ValidatorContext {
	public abstract Validateable getValidateable(Class<?> paramClass);
}