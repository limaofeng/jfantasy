package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 岗位
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:48
 */
@Entity
@Table(name = "AUTH_JOB")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Job extends BaseBusEntity {

    private static final long serialVersionUID = -7020427994563623645L;

    @Id
    @Column(name = "ID")
    private Long id;
    /**
     * 岗位编码
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 岗位名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 岗位描述信息
     */
    @Column(name = "DESCRIPTION")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
