package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.account.Consts;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.dao.UserInfoDao;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.session.WeiXinSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class UserInfoWeiXinService implements InitializingBean{

    @Autowired
    private WeiXinSessionFactory factory;

    private WeiXinSession session;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        session = factory.openSession(Consts.appid);
    }

    public UserInfo getUserInfo(String openId) {
        return userInfoDao.findUniqueBy("openId", openId);
    }
    public UserInfo save(UserInfo ui) {
        return userInfoDao.save(ui);
    }
    public void delete(Long... ids) {
        for(Long id:ids){
            userInfoDao.delete(id);
        }
    }
    public void deleteByOpenId(String openId){
        UserInfo ui=this.getUserInfo(openId);
        this.delete(ui.getId());
    }
    public Pager<UserInfo> findPager(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        return this.userInfoDao.findPager(pager, filters);
    }

    /**
     * 刷新所有的用户信息
     * @throws WeiXinException
     */
    public void refresh() throws WeiXinException {
        List<User> list=session.getUsers();
        for (User u :list) {
            UserInfo ui=this.getUserInfo(u.getOpenId());
            UserInfo unew=transfiguration(u);
            if(ui!=null) unew.setId(ui.getId());
            this.userInfoDao.save(unew);
        }
    }
    /**
     * 通过openId刷新用户信息
     *
     */
    public UserInfo refresh(String openId){
        UserInfo ui = getUserInfo(openId);
        if(ui == null) {
            ui = transfiguration(session.getUser(openId));
            this.userInfoDao.save(ui);
        }
        return ui;
    }


    /**
     * 通过安全连接的code换取微信用户信息
     * @param code
     * @return
     */
    public UserInfo authUserInfo(String code){
        return transfiguration(session.getAuthorizedUser(code));
    }




    /**
     * 设置用户列表的未读信息数量
     * @param list
     */
    public void countUnReadSize(List<UserInfo> list) {
        for (UserInfo u : list) {
            setUnReadSize(u);
        }
    }

    /**
     * 设置用户的未读信息数量
     * @param u
     */
    public void setUnReadSize(UserInfo u) {
        List query = userInfoDao.createSQLQuery("SELECT COUNT(*) c FROM wx_message WHERE create_time>? and openid=?", u.getLastLookTime(), u.getOpenId()).list();
        if (query.size() != 0) {
            u.setUnReadSize(Integer.parseInt(query.get(0).toString()));
        }
    }

    /**
     * 刷新最后查看事件
     * @param ui
     */
    public void refreshMessage(UserInfo ui) {
        userInfoDao.batchSQLExecute("update wx_user_info set last_look_time=last_message_time  where openid=?", ui.getOpenId());
        ui.setLastLookTime(ui.getLastMessageTime());
    }

    /**
     * 转换user到userinfo
     * @param u
     * @return
     */
    public UserInfo transfiguration(User u){
        if (u == null) {
            return null;
        }
        UserInfo user = new UserInfo();
        user.setOpenId(u.getOpenId());
        user.setAvatar(u.getAvatar());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());
        user.setProvince(u.getProvince());
        user.setLanguage(u.getLanguage());
        user.setNickname(u.getNickname());
        user.setSex(u.getSex().getValue());
        user.setSubscribe(u.isSubscribe());
        if (u.getSubscribeTime() != null) {
            user.setSubscribeTime(u.getSubscribeTime().getTime());
        }
        user.setUnionId(u.getUnionid());
        return user;
    }

}
