package com.fantasy.wx.framework.core;

import com.fantasy.wx.framework.session.AccountDetails;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;


public class WeiXinDetails {
    private WeiXinService weiXinService;

    public WeiXinDetails(AccountDetails accountDetails) {
        if (AccountDetails.Type.enterprise == accountDetails.getType()) {
            WxCpService wxCpService = new WxCpServiceImpl();
            WxCpInMemoryConfigStorage wxMpConfigStorage = new WxCpInMemoryConfigStorage();
            wxMpConfigStorage.setCorpId(accountDetails.getAppId());
            wxMpConfigStorage.setCorpSecret(accountDetails.getSecret());
            wxMpConfigStorage.setToken(accountDetails.getToken());
            wxMpConfigStorage.setAesKey(accountDetails.getAesKey());
            wxCpService.setWxCpConfigStorage(wxMpConfigStorage);
            weiXinService = new WeiXinCpService(wxCpService, wxMpConfigStorage);
        } else {
            WxMpService wxMpService = new WxMpServiceImpl();
            WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
            wxMpConfigStorage.setAppId(accountDetails.getAppId());
            wxMpConfigStorage.setSecret(accountDetails.getSecret());
            wxMpConfigStorage.setToken(accountDetails.getToken());
            wxMpConfigStorage.setAesKey(accountDetails.getAesKey());
            wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
            weiXinService = new WeiXinMpService(wxMpService, wxMpConfigStorage);
        }
    }

    public WeiXinService getWeiXinService() {
        return weiXinService;
    }

}
