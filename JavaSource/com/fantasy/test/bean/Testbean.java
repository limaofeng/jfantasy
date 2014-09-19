package com.fantasy.test.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hebo on 2014/9/18.
 */
@Entity
@Table(name = "TESTBEAN")
public class Testbean{

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
}
