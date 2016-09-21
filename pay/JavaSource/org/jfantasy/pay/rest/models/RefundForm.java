package org.jfantasy.pay.rest.models;


import java.math.BigDecimal;

/**
 * 退款表单
 */
public class RefundForm {

    /**
     * 退款金额
     */
    private BigDecimal amount;
    /**
     * 备注
     */
    private String remark;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
