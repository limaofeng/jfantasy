package com.fantasy.weixin.ws;

import com.fantasy.weixin.ws.dto.UserInfoDTO;

/**
 * Created by zzzhong on 2015/1/4.
 */
public interface IUserService {
    /**
     * 通过openId获取微信用户信息
     * @param openId 微信用户的唯一表示
     * @return 微信用户
     */
    public UserInfoDTO getUserInfo(String openId);

    /**
     * 通过code获取 用户信息
     * @param code 微信回调的code值
     * @return 微信用户信息
     */
    public UserInfoDTO authUserInfo(String code);

}
