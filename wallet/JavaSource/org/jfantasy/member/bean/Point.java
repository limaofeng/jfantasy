package org.jfantasy.member.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import java.util.Date;

/**
 * 积分
 */
public class Point extends BaseBusEntity {

    /**
     * 积分
     */
    private Long number;
    /**
     * 过期时间
     */
    private Date expire;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
