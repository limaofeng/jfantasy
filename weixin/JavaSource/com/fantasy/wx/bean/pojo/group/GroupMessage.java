package com.fantasy.wx.bean.pojo.group;

/**
 * 群发消息父类
 * Created by zzzhong on 2014/7/15.
 */
public class GroupMessage {
    private String[] touser;
    private String msgtype;

    public String[] getTouser() {
        return touser;
    }

    public void setTouser(String[] touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
