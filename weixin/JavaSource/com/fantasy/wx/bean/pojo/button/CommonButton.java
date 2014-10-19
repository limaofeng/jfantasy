package com.fantasy.wx.bean.pojo.button;

/**
 * 普通按钮（子按钮）
 * Created by zzzhong on 2014/6/30.
 */
public class CommonButton extends Button {
    private String type;
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
