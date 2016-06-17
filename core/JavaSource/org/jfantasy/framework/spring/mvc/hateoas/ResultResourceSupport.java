package org.jfantasy.framework.spring.mvc.hateoas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

public class ResultResourceSupport<T> extends ResourceSupport {

    private T model;

    @JsonCreator
    public ResultResourceSupport(T model) {
        this.model = model;
    }

    @JsonUnwrapped
    public T getModel() {
        return this.model;
    }

}
