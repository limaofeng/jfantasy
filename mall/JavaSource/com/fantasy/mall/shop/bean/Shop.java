package com.fantasy.mall.shop.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 店铺表
 */
@Entity
@Table(name = "MALL_SHOP")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Shop extends BaseBusEntity {

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 店铺名称
     */
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
