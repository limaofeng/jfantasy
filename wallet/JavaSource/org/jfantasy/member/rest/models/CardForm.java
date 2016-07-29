package org.jfantasy.member.rest.models;


import io.swagger.annotations.ApiModelProperty;

public class CardForm {

    @ApiModelProperty("卡号")
    private String no;
    @ApiModelProperty("密码")
    private String password;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
