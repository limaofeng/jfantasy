package org.jfantasy.test.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Properties;

public class Department {
    private String name;
    private String pm;
    private Properties otherProperties = new Properties(); //otherProperties用来存放Department中未定义的json字段
    //指定json反序列化创建Department对象时调用此构造函数
    @JsonCreator
    public Department(@JsonProperty("name") String name){
        this.name = name;
    }

    //将company.json中projectManager字段关联到getPm方法
    @JsonProperty("projectManager")
    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getName() {
        return name;
    }

    public Object get(String key) {
        return otherProperties.get(key);
    }

    //得到所有Department中未定义的json字段的
    @JsonAnyGetter
    public Properties any() {
        return otherProperties;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        otherProperties.put(key, value);
    }

}
