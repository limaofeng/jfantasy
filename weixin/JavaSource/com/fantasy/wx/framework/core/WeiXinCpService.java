package com.fantasy.wx.framework.core;

import com.fantasy.wx.framework.exception.WeiXinException;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;

public class WeiXinCpService implements WeiXinService {

    private WxCpService wxCpService;

    public WeiXinCpService(WxCpService wxCpService){
        this.wxCpService = wxCpService;
    }
    
    @Override
    public String getJsapiTicket() throws WeiXinException {
        try {
            return wxCpService.getJsapiTicket();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String getJsapiTicket(boolean forceRefresh) throws WeiXinException {
        try {
            return wxCpService.getJsapiTicket(forceRefresh);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public Jsapi.Signature createJsapiSignature(String url) throws WeiXinException {
        try {
            WxJsapiSignature wxJsapiSignature = wxCpService.createJsapiSignature(url);
            return new Jsapi.Signature(wxJsapiSignature.getNoncestr(), wxJsapiSignature.getJsapiTicket(), wxJsapiSignature.getTimestamp(), wxJsapiSignature.getUrl(), wxJsapiSignature.getSignature());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

}
