package com.fantasy.wx.exception;


import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * 微信异常
 * Created by zzzhong on 2014/12/4.
 */
public class WxException extends Exception{

    private WxErrorInfo error;
    public WxException(WxErrorInfo error) {
        super(error.toString());
        this.error = error;
    }

    public static WxException wxExceptionBuilder(WxErrorException e){
        WxErrorInfo wxError=new WxErrorInfo();
        WxError error=e.getError();
        wxError.setErrorCode(error.getErrorCode());
        wxError.setErrorMsg(error.getErrorMsg());
        wxError.setJson(error.getJson());
        return new WxException(wxError);
    }

    public WxErrorInfo getError() {
        return error;
    }

    public void setError(WxErrorInfo error) {
        this.error = error;
    }

}
