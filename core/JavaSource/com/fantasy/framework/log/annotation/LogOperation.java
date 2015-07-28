package com.fantasy.framework.log.annotation;

import org.springframework.util.Assert;

public class LogOperation {

    private String condition = "";
    private String type = "";
    private String name = "";
    private String text = "";

    public String getCondition() {
        return condition;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setCondition(String condition) {
        Assert.notNull(condition);
        this.condition = condition;
    }

    public void setType(String type) {
        Assert.notNull(type);
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LogOperation && toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return getOperationDescription().toString();
    }

    protected StringBuilder getOperationDescription() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("[");
        result.append(this.text);
        result.append(" ] condition='");
        result.append(this.condition);
        result.append("' | type='");
        result.append(this.type);
        result.append("'");
        return result;
    }

}
