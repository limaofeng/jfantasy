package org.jfantasy.pay.rest.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("退款表单")
public class RefundForm {

    @ApiModelProperty(value = "退款金额", required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "备注")
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
