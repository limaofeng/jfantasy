package com.fantasy.framework.dao;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseBusEntity implements Serializable {

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    @Column(updatable = false, name = "CREATOR", length = 20)
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "CREATE_TIME")
    private Date createTime;
    /**
     * 最后修改人
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "MODIFIER", length = 20)
    private String modifier;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(hidden = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    public Date getCreateTime() {
        if (this.createTime == null) {
            return null;
        }
        return (Date) this.createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    public Date getModifyTime() {
        if (this.modifyTime == null) {
            return null;
        }
        return (Date) this.modifyTime.clone();
    }

    public void setModifyTime(Date modifyTime) {
        if (modifyTime == null) {
            this.modifyTime = null;
        } else {
            this.modifyTime = (Date) modifyTime.clone();
        }
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

}