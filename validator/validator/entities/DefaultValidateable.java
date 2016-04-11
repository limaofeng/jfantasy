package org.jfantasy.framework.util.validator.entities;

import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.validator.Validateable;
import org.jfantasy.framework.util.validator.Validator;
import org.jfantasy.framework.util.validator.exception.Error;

public class DefaultValidateable implements Validateable {
    private Validateable superValidateable;
    private Map<String, Validator> validators = new HashMap<String, Validator>();
    private Map<String, List<FieldValidator>> fieldValidators = new HashMap<String, List<FieldValidator>>();

    private Map<String, Map<String, List<FieldValidator>>> methodFieldValidatorsCache = new HashMap<String, Map<String, List<FieldValidator>>>();

    public Map<String, List<String>> validate(String method, Map<String, ?> map) {
        Map<String, List<String>> errorMsg = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<FieldValidator>> entry : getFieldValidators(method).entrySet()) {
            String field = entry.getKey();
            for (FieldValidator fieldValidator : entry.getValue()) {
                Object value = OgnlUtil.getInstance().getValue(field, map);
                if (value instanceof List<?>) {
                    int i = 0;
                    for (Iterator<?> localIterator3 = ((List<?>) value).iterator(); localIterator3.hasNext(); ) {
                        Object v = localIterator3.next();
                        validate(this, fieldValidator, field + "[" + i++ + "]", errorMsg, v);
                    }
                } else {
                    validate(this, fieldValidator, field, errorMsg, value);
                }
            }
        }
        return errorMsg;
    }

    public static void validate(Validateable validateable, FieldValidator fieldValidator, String field, Map<String, List<String>> errorMsg, Object value) {
        Error[] errors = fieldValidator.validate(validateable, value);
        if (errors != null) {
            for (Error error : errors) {
                if (error.isStack()) {
                    String fieldName = field + "." + error.getField();
                    getErrorList(errorMsg, fieldName).add(error.getMessage());
                } else {
                    getErrorList(errorMsg, field).add(error.getMessage());
                }
            }
        }
    }

    public static List<String> getErrorList(Map<String, List<String>> errorMsg, String key) {
        if (!errorMsg.containsKey(key)) {
            errorMsg.put(key, new ArrayList<String>());
        }
        return errorMsg.get(key);
    }

    private Map<String, List<FieldValidator>> getFieldValidators(String method) {
        synchronized (this.methodFieldValidatorsCache) {
            if (!this.methodFieldValidatorsCache.containsKey(method)) {
                Map<String, List<FieldValidator>> map = new HashMap<String, List<FieldValidator>>();
                for (Map.Entry<String, List<FieldValidator>> entry : this.fieldValidators.entrySet()) {
                    String key = (String) entry.getKey();
                    if (RegexpUtil.find(key, new String[]{"^" + method})) {
                        map.put(RegexpUtil.replaceFirst(key, "^" + method + ".", ""), entry.getValue());
                    }
                }
                this.methodFieldValidatorsCache.put(method, map);
            }
        }
        return this.methodFieldValidatorsCache.get(method);
    }

    public void addFieldValidators(String method, String field, List<FieldValidator> fieldValidators) {
        this.fieldValidators.put(method + "." + field, fieldValidators);
    }

    public void addValidator(String name, Validator validator) {
        this.validators.put(name, validator);
    }

    public Validator getValidator(String type) {
        Validator validator = (Validator) this.validators.get(type);
        if ((validator == null) && (this.superValidateable != null)) {
            validator = this.superValidateable.getValidator(type);
            this.validators.put(type, validator);
        }
        return validator;
    }

    public void setSuper(Validateable validateable) {
        this.superValidateable = validateable;
    }
}