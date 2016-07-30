package org.jfantasy.express.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@ApiModel("快速公司")
@Entity
@Table(name = "EXPRESS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Express extends BaseBusEntity {

    private static final long serialVersionUID = 10595703086045998L;

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    private String id;
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;// 物流公司名称
    @Column(name = "URL", length = 50)
    private String url;// 物流公司网址
    @Column(name = "SORT")
    private Integer sort;// 排序
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;// 介绍
    /**
     * 快递接口
     */
    @Column(name = "BILL_INTERFACE_ID")
    private String billInterfaceId;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillInterfaceId() {
        return billInterfaceId;
    }

    public void setBillInterfaceId(String billInterfaceId) {
        this.billInterfaceId = billInterfaceId;
    }
}
