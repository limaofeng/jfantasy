package org.jfantasy.pay.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.ProjectType;

import javax.persistence.*;

/**
 * 交易项目
 */
@Entity
@Table(name = "PAY_PROJECT")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Project extends BaseBusEntity {

    public final static String ORDER_PAYMENT = "payment";
    public final static String ORDER_REFUND = "refund";
    public final static String CARD_INPOUR = "card_inpour";

    /**
     * 编码
     */
    @Id
    @Column(name = "CODE", updatable = false)
    private String key;
    /**
     * 项目类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ProjectType type;
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

    public Project(String key) {
        this.key = key;
    }

    public Project() {
    }

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

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

}
