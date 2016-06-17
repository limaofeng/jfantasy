package org.jfantasy.wx.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.bean.UserKey;
import org.jfantasy.wx.dao.UserDao;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("wx.userService")
@Transactional
public class UserService implements InitializingBean {

    @Autowired
    private UserDao userDao;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public User get(UserKey key) {
        return userDao.get(key);
    }

    public User save(User ui) {
        return userDao.save(ui);
    }

    public void delete(UserKey... keys) {
        for (UserKey key : keys) {
            userDao.delete(key);
        }
    }

    public void delete(UserKey key) {
        this.userDao.delete(key);
    }

    public Pager<User> findPager(Pager<User> pager, List<PropertyFilter> filters) {
        return this.userDao.findPager(pager, filters);
    }

    /**
     * 通过openId刷新用户信息
     */
    private User refresh(String appid,String openId) throws WeiXinException {
        User ui = get(UserKey.newInstance(appid,openId));
        if (ui != null) {
            return ui;
        }
        org.jfantasy.wx.framework.message.user.User user = WeiXinSessionUtils.getCurrentSession().getUser(openId);
        if (user == null) {
            return null;
        }
        ui = transfiguration(user);
        ui.setAppId(appid);
        this.userDao.save(ui);
        return ui;
    }


    /**
     * 转换user到userinfo
     *
     * @param u
     * @return
     */
    public User transfiguration(org.jfantasy.wx.framework.message.user.User u) {
        if (u == null) {
            return null;
        }
        User user = new User();
        user.setOpenId(u.getOpenId());
        user.setAvatar(u.getAvatar());
        user.setCity(u.getCity());
        user.setCountry(u.getCountry());
        user.setProvince(u.getProvince());
        user.setLanguage(u.getLanguage());
        user.setNickname(u.getNickname());
        user.setSex(u.getSex());
        user.setSubscribe(u.isSubscribe());
        if (u.getSubscribeTime() != null) {
            user.setSubscribeTime(u.getSubscribeTime().getTime());
        }
        user.setUnionId(u.getUnionid());
        return user;
    }

    public User checkCreateMember(String appid,String openId) throws WeiXinException {
        return refresh(appid,openId);
    }

}
