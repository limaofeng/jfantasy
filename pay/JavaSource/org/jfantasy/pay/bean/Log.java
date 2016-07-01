package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.OwnerType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAY_LOG", uniqueConstraints = {@UniqueConstraint(columnNames = {"OWNER_TYPE", "OWNER_ID"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "creator", "createTime", "modifier", "modifyTime"})
public class Log extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 所有者类型
     */
    @Column(name = "OWNER_TYPE",length = 20)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    /**
     * 所有者id
     */
    @Column(name = "OWNER_ID",length = 30)
    private String ownerId;
    /**
     * 订单ID
     */
    @ApiModelProperty("订单详情")
    @Column(name = "ORDER_KEY",length = 30)
    private String orderKey;
    /**
     * 支付状态
     */
    @ApiModelProperty("支付状态")
    @Column(name = "STATUS", nullable = false, updatable = false)
    private String status;
    /**
     * 备注
     */
    @Column(name = "NOTES", updatable = false)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getLogTime() {
        return this.getCreateTime();
    }

}
