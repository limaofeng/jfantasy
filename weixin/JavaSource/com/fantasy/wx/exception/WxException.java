package com.fantasy.wx.exception;


/**
 * Created by zzzhong on 2014/12/4.
 */
public class WxException extends Exception{

    private WxErrorInfo error;
    public WxException(WxErrorInfo error) {
        super(error.toString());
        this.error = error;
    }

    public WxErrorInfo getError() {
        return error;
    }

    public void setError(WxErrorInfo error) {
        this.error = error;
    }

}
