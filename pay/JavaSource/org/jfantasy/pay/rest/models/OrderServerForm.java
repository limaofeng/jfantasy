package org.jfantasy.pay.rest.models;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.order.entity.enums.CallType;

import java.util.HashMap;
import java.util.Map;

public class OrderServerForm {

    private CallType callType;
    private String orderType;
    private String title;
    private String description;
    /**
     * callType = rpc 时必填
     */
    private String host;
    /**
     * callType = rpc 时必填
     */
    private int port;
    /**
     * callType = restful 时必填
     */
    private String url;
    /**
     * 访问授权的 token
     */
    private String token;

    @JsonAnyGetter
    public Map<String,Object> getProperties() {
        Map<String,Object> props = new HashMap<>();
        if (CallType.restful == callType) {
            props.put(OrderServer.PROPS_RESTURL, this.url);
        } else if (CallType.rpc == callType) {
            props.put(OrderServer.PROPS_HOST, this.host);
            props.put(OrderServer.PROPS_PORT, this.port);
        }
        return props;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
