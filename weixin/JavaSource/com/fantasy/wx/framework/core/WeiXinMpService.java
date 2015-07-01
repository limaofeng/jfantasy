package com.fantasy.wx.framework.core;

import com.fantasy.wx.framework.exception.WeiXinException;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

public class WeiXinMpService implements WeiXinService {

    private WxMpService wxMpService;

    public WeiXinMpService(WxMpService wxMpService){
        this.wxMpService = wxMpService;
    }
    
    @Override
    public String getJsapiTicket() throws WeiXinException {
        try {
            return wxMpService.getJsapiTicket();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String getJsapiTicket(boolean forceRefresh) throws WeiXinException {
        try {
            return wxMpService.getJsapiTicket(forceRefresh);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public Jsapi.Signature createJsapiSignature(String url) throws WeiXinException {
        try {
            WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
            return new Jsapi.Signature(wxJsapiSignature.getNoncestr(), wxJsapiSignature.getJsapiTicket(), wxJsapiSignature.getTimestamp(), wxJsapiSignature.getUrl(), wxJsapiSignature.getSignature());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }
}
