package org.jfantasy.framework.spring.mvc.hateoas;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashMap;
import java.util.Map;

public class ResultResourceSupport<T> extends ResourceSupport {

    private T model;
    private Map<String,Object> properties = new HashMap<>();

    @JsonCreator
    public ResultResourceSupport(T model) {
        this.model = model;
    }

    @JsonAnyGetter
    public Map<String,Object> getProperties() {
        return this.properties;
    }

    @JsonUnwrapped
    public T getModel() {
        return this.model;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        this.properties.put(key, value);
    }

}
