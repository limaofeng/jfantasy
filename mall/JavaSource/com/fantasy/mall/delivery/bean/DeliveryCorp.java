package com.fantasy.mall.delivery.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 物流公司
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-16 上午11:10:19
 */
@ApiModel("物流公司")
@Entity
@Table(name = "MALL_DELIVERY_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "deliveryTypes"})
public class DeliveryCorp extends BaseBusEntity {

    private static final long serialVersionUID = 10595703086045998L;

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;// 物流公司名称
    @Column(name = "URL", length = 50)
    private String url;// 物流公司网址
    @Column(name = "SORT")
    private Integer sort;// 排序
    @Column(name = "DESCRIPTION", length = 3000)
    private String description;// 介绍
    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "defaultDeliveryCorp", fetch = FetchType.LAZY)
    private List<DeliveryType> deliveryTypes;// 配送方式

    public DeliveryCorp() {
    }

    public DeliveryCorp(Long id) {
        this.id = id;
    }

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

    public List<DeliveryType> getDeliveryTypes() {
        return deliveryTypes;
    }

    public void setDeliveryTypes(List<DeliveryType> deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}