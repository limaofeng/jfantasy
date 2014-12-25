package com.fantasy.wx.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.wx.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by zzzhong on 2014/9/23.
 */
public class WeixinAction extends ActionSupport {

    @Autowired
    public WeiXinService weiXinService;

    public String operationUrl() throws IOException {
        weiXinService.onEvent(request, response);
        return NONE;
    }

}
