package com.fantasy.wx.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.wx.service.CoreService;
import com.fantasy.wx.util.SignUtil;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by zzzhong on 2014/9/23.
 */
public class WeixinAction extends ActionSupport{
    @Resource
    private CoreService coreService;

    public String operationUrl()throws IOException{
        this.request.setCharacterEncoding("utf-8");
        this.response.setCharacterEncoding("utf-8");
        String method = request.getMethod();
        if(method.equals("POST")){
            // 调用核心业务类接收消息、处理消息
            String respMessage = coreService.processRequest(request);

            response.getWriter().print(respMessage);
        }else{
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");

            // 随机字符串
            String echostr = request.getParameter("echostr");
            if (SignUtil.checkSignature("haolue_weixin", signature, timestamp, nonce)) {
                response.getWriter().print(echostr);
            }
        }
        WebUtil.getMethod(this.request);
        this.response.getWriter().print("");
        return NONE;
    }
}
