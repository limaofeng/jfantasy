package org.jfantasy.member.bean;


public class Points {
    /**
     * 总积分
     */
    private Long total;
    /**
     * 即将过期的积分
     */
    private Long willExpire;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getWillExpire() {
        return willExpire;
    }

    public void setWillExpire(Long willExpire) {
        this.willExpire = willExpire;
    }
}
