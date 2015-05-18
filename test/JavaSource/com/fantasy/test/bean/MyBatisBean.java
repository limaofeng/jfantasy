package com.fantasy.test.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TEST_BEAN")
public class MyBatisBean {

    @Id
    @Column(name = "T_KEY")
    private String key;

    @Column(name = "T_VALUE")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TyBatisbean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
