package org.jfantasy.wx.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.wx.framework.session.AccountDetails;

import javax.persistence.*;

/**
 * 微信公众号设置
 * Created by zzzhong on 2014/6/18.
 */
@ApiModel("微信公众账号")
@Entity
@Table(name = "WX_ACCOUNT")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Account extends BaseBusEntity implements AccountDetails {

    //开发者凭证
    /** 开发者凭证 **/
    @Id
    @Column(name = "APP_ID", length = 200)
    private String appId;
    /**
     * 原始ID
     */
    /** 原始ID **/
    @Column(name = "PRIMITIVE_ID", length = 200)
    private String primitiveId;
    /** 密钥 **/
    @Column(name = "APP_SECRET", length = 200)
    private String secret;
    /**
     * 公众号类型
     */
    /** 公众号类型 **/
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20)
    private Type type;
    /**
     * 公众号名称
     */
    /** 公众号名称 **/
    @Column(name = "NAME", length = 100)
    public String name;
    @ApiModelProperty(value = "token", notes = "微信服务器配置的token")
    //微信服务器配置的token
    @Column(name = "TOKEN_NAME", length = 200)
    private String token;
    @ApiModelProperty(value = "aes Key", notes = "微信生成的 ASEKey ")
    @Column(name = "AES_KEY", length = 200)
    private String aesKey;
    @ApiModelProperty(value = "代理ID", notes = "企业号才需要配置该属性")
    @Column(name = "AGENT_ID", length = 200)
    private String agentId;

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getAesKey() {
        return this.aesKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrimitiveId() {
        return primitiveId;
    }

    @Override
    public String getAgentId() {
        return this.agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setPrimitiveId(String primitiveId) {
        this.primitiveId = primitiveId;
    }
}
