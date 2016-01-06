package org.jfantasy.attr.typeConverter;

import org.jfantasy.security.bean.User;

public class TypeBean {
    private String key;
    private User user;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TypeBean{" +
                "key='" + key + '\'' +
                ", user=" + user +
                '}';
    }
}
