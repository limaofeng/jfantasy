package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.user.bean.UserInfo;
import me.chanjar.weixin.common.exception.WxErrorException;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IUserInfoService {

    /**
     * 获取所有未添加的微信粉丝，并刷新粉丝状态
     *
     * @param openId
     * @return
     */
    public List<String> getRefreshOpenId(List<String> openId);

    /**
     * 保存多个用户
     *
     * @param uArray
     */
    public void saveArry(UserInfo[] uArray);

    /**
     * 获取
     *
     * @param openId
     * @return
     */
    public UserInfo getUserInfo(String openId);

    /**
     * 保存
     *
     * @param ui
     */
    public void save(UserInfo ui);

    /**
     * 查询分组的分页对象
     *
     * @param pager
     * @return 分页对象
     */
    public Pager<UserInfo> findPager(Pager<UserInfo> pager, List<PropertyFilter> filters);

    /**
     * 刷新粉丝列表
     */
    public void refresh() throws WeiXinException;

    /**
     * 刷新用户对象
     *
     * @param openId
     * @throws WxErrorException
     */
    public UserInfo refresh(String openId) throws WeiXinException;

    /**
     * 删除用户对象
     *
     * @param openId
     */
    public void delete(String openId);

    /**
     * 查询所有用户未读的消息数
     *
     * @param list
     */
    public void countUnReadSize(List<UserInfo> list);

    /**
     * 获取某个用户的未读消息数
     *
     * @param u
     */
    public void setUnReadSize(UserInfo u);

    /**
     * 刷新用户最后查看消息时间
     *
     * @param ui
     */
    public void refreshMessage(UserInfo ui);

}
