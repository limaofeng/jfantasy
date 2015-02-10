package com.fantasy.wx.framework.exception;


/**
 * 微信错误码说明
 * http://mp.weixin.qq.com/wiki/index.php?title=全局返回码说明
 *
 * @author zzz
 */
public class WxErrorInfo {
    private int errorCode;

    private String errorMsg;

    private String json;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "微信错误 errcode=" + errorCode + ", errmsg=" + errorMsg + "\njson:" + json;
    }

}
