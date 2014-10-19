package com.fantasy.wx.bean.pojo.button;

/**
 * view类型的菜单
 * Created by zzzhong on 2014/6/30.
 */
public class ViewButton extends Button {
    private String type;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
