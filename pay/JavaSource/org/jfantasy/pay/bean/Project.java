package org.jfantasy.pay.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 交易项目
 */
@Entity
@Table(name = "PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project extends BaseBusEntity {

    /**
     * 编码
     */
    @Id
    @Column(name = "CODE", updatable = false)
    private String key;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
