package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Entity
@Table(name = "MEM_TAG", uniqueConstraints = {@UniqueConstraint(name = "UN_MEM_TAG_NAMEORTYPE", columnNames = {"name", "type"})})
@TableGenerator(name = "tag_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_tag:id", valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Tag extends BaseBusEntity {

    private static final long serialVersionUID = -5572944412170364049L;

    @Null(groups = {RESTful.POST.class})
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tag_gen")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;
    /**
     * 合计数量<用于汇总标签分配的数量>
     */
    @Column(name = "TOTAL", nullable = false)
    private Integer total;
    /**
     * 类型
     */
    @Column(name = "TYPE", nullable = false, updatable = false, length = 50)
    private String type;
    /**
     * 所有者Id
     */
    @Column(name = "OWNER_ID", updatable = false, length = 32)
    private String ownerId;
    /**
     * 所有者类型
     */
    @Column(name = "OWNER_TYPE", updatable = false, length = 10)
    private String ownerType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
