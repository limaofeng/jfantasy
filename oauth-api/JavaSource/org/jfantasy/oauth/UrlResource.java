package org.jfantasy.oauth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UrlResource implements Serializable {

    private Long id;

    private List<PermissionRule> rules = new ArrayList<>();

    public List<PermissionRule> getRules() {
        return rules;
    }

    public void setRules(List<PermissionRule> rules) {
        this.rules = rules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void addRule(PermissionRule rule) {
        this.rules.add(rule);
    }
}
