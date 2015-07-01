package com.fantasy.wx.framework.core;

import com.fantasy.wx.framework.exception.WeiXinException;

public class DefaultJsapi implements Jsapi {

    private WeiXinService weiXinService;

    protected DefaultJsapi(final WeiXinService weiXinService) {
        this.weiXinService = weiXinService;
    }

    @Override
    public String getTicket() throws WeiXinException {
        return weiXinService.getJsapiTicket();
    }

    @Override
    public String getTicket(boolean forceRefresh) throws WeiXinException {
        return weiXinService.getJsapiTicket(forceRefresh);
    }

    @Override
    public Signature signature(String url) throws WeiXinException {
        return weiXinService.createJsapiSignature(url);

    }

}
