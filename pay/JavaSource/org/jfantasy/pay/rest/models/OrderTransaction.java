package org.jfantasy.pay.rest.models;

import org.jfantasy.pay.bean.Project;

public class OrderTransaction {

    public enum Type {

        payment(Project.ORDER_PAYMENT), refund(Project.ORDER_REFUND);

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
