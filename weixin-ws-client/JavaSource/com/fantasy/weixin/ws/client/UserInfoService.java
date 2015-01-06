package com.fantasy.weixin.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.weixin.ws.IUserService;
import com.fantasy.weixin.ws.dto.UserInfoDTO;

/**
 * Created by zzzhong on 2015/1/6.
 */
public class UserInfoService  extends WebServiceClient implements IUserService {
    public UserInfoService() {
        super("UserInfoService");
    }

    @Override
    public UserInfoDTO getUserInfo(String openId) {
        return super.invokeOption("getUserInfo",new Object[] { openId },UserInfoDTO.class);
    }

    @Override
    public UserInfoDTO authUserInfo(String code) {
        return super.invokeOption("getUserInfo",new Object[] { code },UserInfoDTO.class);
    }
}
