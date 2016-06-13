package org.jfantasy.pay.order.entity.enums;

/**
 * 支付状态
 */
public enum PaymentStatus {

    /**
     * 准备支付 (下一步状态 为 关闭/成功/失败)
     */
    ready("开始"),
    /**
     * 支付关闭(最终状态 , 不可继续操作)
     */
    close("关闭"),
    /**
     * 支付失败(最终状态 , 不可继续操作)
     */
    failure("失败"),
    /**
     * 支付成功(可以退款)
     */
    success("成功"),
    /**
     * 支付完成(最终状态 , 不可继续操作)
     */
    finished("完成");

    private String value;

    PaymentStatus(String value){
        this.value = value;
    }

    public String value(){
       return this.value;
    }

}
