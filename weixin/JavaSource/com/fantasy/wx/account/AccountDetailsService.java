package com.fantasy.wx.account;

import com.fantasy.wx.exception.AppidNotFoundException;
import com.fantasy.wx.session.AccountDetails;

import java.util.List;

/**
 * 微信公众号接口
 */
public interface AccountDetailsService {

    /**
     * 通过 appid 获取微信号配置信息
     *
     * @param appid appid
     * @return AccountDetails
     * @throws AppidNotFoundException
     */
    AccountDetails loadAccountByAppid(String appid) throws AppidNotFoundException;

    /**
     * 获取全部的微信公众号信息
     *
     * @return List<AccountDetails>
     */
    List<AccountDetails> getAll();

}
