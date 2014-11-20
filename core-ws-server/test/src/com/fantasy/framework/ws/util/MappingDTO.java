package com.fantasy.framework.ws.util;


public class MappingDTO {

    @Property("name")
    private String username;

    @Property("pwd")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
