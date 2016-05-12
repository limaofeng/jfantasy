package org.jfantasy.pay.rest.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.enums.RefundStatus;

@ApiModel("退款表单")
public class RefundForm01 {

    @ApiModelProperty(value = "退款状态", required = true)
    private RefundStatus status;
    @ApiModelProperty(value = "备注")
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

    public Refund toRefund(String sn) {
        Refund refund = new Refund();
        refund.setSn(sn);
        refund.setStatus(status);
        return refund;
    }

}
