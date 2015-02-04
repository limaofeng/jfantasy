package com.fantasy.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yhx on 2015/2/3.
 */
@Entity
@Table(name="ORG_POSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer","description"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrgPosition {

    /**
     * 岗位Id
     */
    @Id
    @Column(name = "CODE")
    private String id;


    /**
     * 岗位名称
     */
    @Column(name="NAME")
    private String name;



    /**
     * 岗位描述
     */
    @Column(name="DESCRIPTION")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
