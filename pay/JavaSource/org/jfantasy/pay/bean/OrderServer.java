package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.order.entity.enums.CallType;

import javax.persistence.*;
import java.util.Properties;

/**
 * 注册的订单服务器
 */
@ApiModel("订单服务")
@Entity
@Table(name = "PAY_ORDER_SERVER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderServer extends BaseBusEntity {

    /**
     * 主机地址<br/>
     */
    public final static String PROPS_HOST = "host";
    /**
     * 主机端口<br/>
     */
    public final static String PROPS_PORT = "port";
    /**
     * 服务的完整地址<br/>
     */
    public final static String PROPS_RESTURL = "url";
    /**
     * 调用服务时,需要提供的身份信息
     */
    public final static String PROPS_TOKEN = "token";

    /**
     * 服务的订单类型
     */
    @Id
    @Column(name = "TYPE", updatable = false, length = 50)
    private String type;
    /**
     * 服务名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 调用方式
     */
    @ApiModelProperty("调用方式")
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 10, nullable = false)
    private CallType callType;
    /**
     * 配置参数
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "PROPERTIES", columnDefinition = "MediumBlob")
    private Properties properties;
    /**
     * 详细介绍
     */
    @ApiModelProperty("介绍")
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private boolean enabled;

    @JsonAnyGetter
    public Properties getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void set(String key, String value) {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        this.properties.put(key, value);
    }

    @Transient
    public String get(String key) {
        if (this.properties == null) return null;
        return this.properties.getProperty(key);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
