package com.fantasy.wx.bean.req;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zzzhong on 2014/9/26.
 */
@Entity
@Table(name = "WX_MESSAGE_EVENT")
public class EventMessage extends BaseMessage {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    private String evnet;

    private String evnetKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
