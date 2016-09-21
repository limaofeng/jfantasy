package org.jfantasy.pay.rest.models;


import org.jfantasy.pay.order.entity.enums.RefundStatus;

/**
 * 退款表单
 */
public class RefundForm01 {

    /**
     * 退款状态
     */
    private RefundStatus status;
    /**
     * 备注
     */
    private String remark;

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
