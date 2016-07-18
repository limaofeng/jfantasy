package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 会员等级
 */
@Entity
@Table(name = "MEM_LEVEL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Level extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private Long id;
    /**
     * 等级名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 所需经验
     */
    @Column(name = "EXP")
    private Long exp;
    /**
     * 等级描述
     */
    @Column(name = "DESCRIPTION", length = 500)
    private String description;

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

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
