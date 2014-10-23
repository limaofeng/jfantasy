package com.fantasy.wx.bean.req;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zzzhong on 2014/9/26.
 */
public class EventMessage extends BaseMessage {

    private String evnet;

    private String evnetKey;


    public String getEvnet() {
        return evnet;
    }

    public void setEvnet(String evnet) {
        this.evnet = evnet;
    }

    public String getEvnetKey() {
        return evnetKey;
    }

    public void setEvnetKey(String evnetKey) {
        this.evnetKey = evnetKey;
    }
}
