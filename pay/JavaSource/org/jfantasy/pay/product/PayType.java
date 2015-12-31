package org.jfantasy.pay.product;

/**
 * 支付类
 */
public enum PayType {
    web("普通网页支付"), wap("手机网页支付"), app("移动端支付");

    private String remark;

    PayType(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
