package com.fantasy.wx.framework.core;


import com.fantasy.wx.framework.exception.WeiXinException;

public interface WeiXinService {

    String getJsapiTicket() throws WeiXinException;

    String getJsapiTicket(boolean forceRefresh) throws WeiXinException;

    Jsapi.Signature createJsapiSignature(String url) throws WeiXinException;
//
//    Long userGetGroup(String openId) throws WeiXinException;
//
//    boolean checkSignature(String timestamp, String nonce, String signature);
}
