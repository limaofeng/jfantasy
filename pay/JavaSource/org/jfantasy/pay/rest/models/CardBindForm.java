package org.jfantasy.pay.rest.models;

import io.swagger.annotations.ApiModelProperty;

public class CardBindForm {

    @ApiModelProperty("密码")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
