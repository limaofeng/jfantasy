package org.jfantasy.pay.order.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 订单 key
 */
public class OrderKey implements Serializable {

    public static OrderKey newInstance(String type, String sn) {
        return new OrderKey(type, sn);
    }

    public static OrderKey newInstance(String key) {
        String[] ar = key.split(":");
        return new OrderKey(ar[0],ar[1]);
    }

    /**
     * 虚拟文件路径
     */
    @Column(name = "TYPE", nullable = false, updatable = false, length = 250)
    private String type;

    @Column(name = "SN", nullable = false, updatable = false, length = 50)
    private String sn;

    public OrderKey() {
    }

    public OrderKey(String type, String sn) {
        super();
        this.type = type;
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getType()).append(this.getSn()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OrderKey) {
            OrderKey key = (OrderKey) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).append(this.getType(), key.getType()).append(this.getSn(), key.getSn()).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.type + ":" + this.sn;
    }

}
