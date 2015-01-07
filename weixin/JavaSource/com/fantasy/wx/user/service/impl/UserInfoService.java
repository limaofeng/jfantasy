package com.fantasy.wx.user.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.user.bean.WxGroup;
import com.fantasy.wx.user.dao.UserInfoDao;
import com.fantasy.wx.user.service.IGroupService;
import com.fantasy.wx.user.service.IUserInfoService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service("wxUserInfoService")
@Transactional
public class UserInfoService implements IUserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private IGroupService iGroupService;
    @Resource
    private WeixinConfigInit config;

    @Override
    public List<String> getRefreshOpenId(List<String> openId) {
        userInfoDao.batchSQLExecute("UPDATE wx_user_info SET subscribe=false");
        UserInfo u = null;
        List<String> sList = new ArrayList<String>();
        for (String s : openId) {
            u = userInfoDao.get(s);
            if (u == null) {
                sList.add(s);
            } else {
                u.setSubscribe(true);
                userInfoDao.save(u);
            }
        }
        return sList;
    }

    @Override
    public void saveArry(UserInfo[] uArray) {
        for (UserInfo u : uArray) {
            save(u);
        }
    }

    @Override
    public UserInfo getUserInfo(String openId) {
        return userInfoDao.get(openId);
    }

    @Override
    public void save(UserInfo ui) {
        userInfoDao.save(ui);
    }

    @Override
    public Pager<UserInfo> findPager(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        return this.userInfoDao.findPager(pager, filters);
    }

    public List<UserInfo> findByGroupId(Long id){
        //this.userInfoDao.findUniqueBy("EQL_")
        return null;
    }

    @Override
    public void refresh() throws WxException {
        WxMpUserList userList = null;
        List<String> refreshList = new ArrayList<String>();
        try{
            do {
                userList = config.getUtil().userList(userList == null ? null : userList.getNextOpenId());
                refreshList.addAll(userList.getOpenIds());
            } while (userList != null && userList.getTotal() > userList.getCount());

            for (String s : getRefreshOpenId(refreshList)) {
                refresh(s);
            }
        }catch (WxErrorException e){
            throw WxException.wxExceptionBuilder(e);
        }

    }

    @Override
    public UserInfo refresh(String openId) throws WxException {
        try{
            UserInfo ui = BeanUtil.copyProperties(new UserInfo(), config.getUtil().userInfo(openId, null));
            Long groupId = iGroupService.getUserGroup(ui.getOpenId());
            if (groupId != -1) {
                ui.setWxGroup(new WxGroup(groupId, null));
            }
            userInfoDao.save(ui);
            return ui;
        }catch (WxErrorException e){
            throw WxException.wxExceptionBuilder(e);
        }
    }

    @Override
    public void delete(String openId) {
        userInfoDao.delete(openId);
    }

    @Override
    public void countUnReadSize(List<UserInfo> list) {
        for (UserInfo u : list) {
            setUnReadSize(u);
        }
    }

    @Override
    public void setUnReadSize(UserInfo u) {
        List query = userInfoDao.createSQLQuery("SELECT COUNT(*) c FROM wx_message WHERE create_time>? and openid=?", u.getLastLookTime(), u.getOpenId()).list();
        if (query.size() != 0) {
            u.setUnReadSize(Integer.parseInt(query.get(0).toString()));
        }
    }

    @Override
    public void refreshMessage(UserInfo ui) {
        userInfoDao.batchSQLExecute("update wx_user_info set last_look_time=last_message_time  where openid=?", ui.getOpenId());
        ui.setLastLookTime(ui.getLastMessageTime());
    }


}
