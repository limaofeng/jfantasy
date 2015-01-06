package com.fantasy.wx.exception;


import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * 微信异常
 * Created by zzzhong on 2014/12/4.
 */
public class WeiXinException extends Exception {

    private WxErrorInfo error;

    public WeiXinException(){}
    public WeiXinException(WxErrorInfo error) {
        super(error.toString());
        this.error = error;
    }

    public WeiXinException(String message) {
        super(message);
    }

    public WeiXinException(String message, Exception e) {
        super(message, e);
    }

    public static WeiXinException wxExceptionBuilder(WxErrorException e) {
        WxErrorInfo wxError = new WxErrorInfo();
        WxError error = e.getError();
        wxError.setErrorCode(error.getErrorCode());
        wxError.setErrorMsg(error.getErrorMsg());
        wxError.setJson(error.getJson());
        return new WeiXinException(wxError);
    }

    public WxErrorInfo getError() {
        return error;
    }

    public void setError(WxErrorInfo error) {
        this.error = error;
    }

}
