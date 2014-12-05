package com.fantasy.framework.ws.util;


public class MappingDTO {

    @Mapping("name")
    private String username;

    @Mapping("pwd")
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
