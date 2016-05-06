package org.jfantasy.wx.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信用用户组
 * Created by zzzhong on 2014/6/19.
 */
@Entity(name= "WxGroup")
@Table(name = "WX_GROUP")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Group  extends BaseBusEntity {
    public Group() {
    }
    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Group(Long id, String name,Long count) {
        this.id = id;
        this.name = name;
        this.count=count;
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    private Long id=-1L;
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
