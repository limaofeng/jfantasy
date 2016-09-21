package org.jfantasy.pay.rest.models;


import org.jfantasy.pay.bean.enums.PointStatus;

public class PointForm {
    /**
     * 状态
     */
    private PointStatus status;
    /**
     * 备注
     */
    private String remark;

    public PointStatus getStatus() {
        return status;
    }

    public void setStatus(PointStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
