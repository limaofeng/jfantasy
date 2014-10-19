package com.fantasy.wx.bean.pojo.code;

/**
 * 临时二维码
 */
public class TempCode extends QCode {
    /**
     * 临时二维码有效时间，以秒为单位。 最大不超过1800。 永久二维码无此字段
     */
    private int expire_seconds;

    public int getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(int expire_seconds) {
        this.expire_seconds = expire_seconds;
    }
}
