package org.jfantasy.security.rest.models;

import java.io.Serializable;

public class MenuIds implements Serializable {
    private String[] ids;

    public MenuIds(String[] ids) {
        this.ids = ids;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
