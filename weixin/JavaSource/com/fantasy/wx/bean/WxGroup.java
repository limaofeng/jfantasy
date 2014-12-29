package com.fantasy.wx.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信用用户组
 * Created by zzzhong on 2014/6/19.
 */
@Entity
@Table(name = "WX_GROUP")
public class WxGroup {
    public WxGroup() {
    }

    public WxGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "ID")
    private Long id = -1L;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COUNT")
    private Long count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
