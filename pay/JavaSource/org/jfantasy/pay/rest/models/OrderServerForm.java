package org.jfantasy.pay.rest.models;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.jfantasy.pay.order.entity.enums.CallType;

import java.util.Properties;

public class OrderServerForm {

    private CallType callType;
    private String orderType;
    private String url;
    private String title;
    private String description;


    @JsonAnyGetter
    public Properties getProperties() {
        Properties props = new Properties();
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
}
