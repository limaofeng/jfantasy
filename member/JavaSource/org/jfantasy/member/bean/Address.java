package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.common.Area;
import org.jfantasy.common.converter.AreaConverter;
import org.jfantasy.common.databind.AreaDeserializer;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Entity
@Table(name = "MEM_ADDRESS")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Address extends BaseBusEntity {

    @Id
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    @Column(name = "ID", updatable = false)
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 地区
     */
    @Column(name = "AREA_STORE", length = 300, nullable = false)
    @Convert(converter = AreaConverter.class)
    @JsonDeserialize(using = AreaDeserializer.class)
    private Area area;
    /**
     * 详细地址
     */
    @Column(name = "DETAILS", length = 200, nullable = false)
    private String details;
    /**
     * 排班所有者类型
     */
    @Column(name = "OWNER_TYPE", length = 20, nullable = false)
    private String ownerType;
    /**
     * 排班所有者ID
     */
    @Column(name = "OWNER_ID", length = 32, nullable = false)
    private String ownerId;

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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}
