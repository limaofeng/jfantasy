package org.jfantasy.member.rest.models;


import org.jfantasy.framework.spring.validation.RESTful;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class TagForm {
    @NotNull(groups = {RESTful.POST.class, RESTful.PATCH.class})
    private String name;
    @Null(groups = RESTful.PATCH.class)
    @NotNull(groups = RESTful.POST.class)
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
