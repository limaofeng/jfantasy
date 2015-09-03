package com.fantasy.security.bean;

import com.fantasy.security.bean.enums.PermissionType;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;

@ApiModel(value = "权限规则", description = "权限规则详情")
public class PermissionRule {
    /**
     * 规则类型
     */
    @ApiModelProperty("规则类型")
    private PermissionType type;
    /**
     * 访问方式
     */
    @ApiModelProperty(value = "Http访问", notes = " 规则类型为 antPath 时,可以设置该属性,其他设置无效")
    private HttpMethod httpMethod;
    /**
     * 规则表达式
     */
    @ApiModelProperty(value = "规则表达式", notes = "根据规则类型,配置不同的值。可以是：正则、URL、IP地址等")
    private String pattern;

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @ApiModelProperty(hidden = true)
    public RequestMatcher getRequestMatcher() {
        return null;
    }

}
