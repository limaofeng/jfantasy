package com.fantasy.wx.user.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.exception.WxErrorInfo;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.user.bean.WxGroup;
import com.fantasy.wx.user.dao.WxGroupDao;
import com.fantasy.wx.user.service.IGroupService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class WxGroupService implements IGroupService {
    @Resource
    private WxGroupDao wxGroupDao;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private WeixinConfigInit config;

    @Override
    public WxGroup save(WxGroup wxGroup) {
        wxGroupDao.save(wxGroup);
        return wxGroup;
    }

    @Override
    public List<WxGroup> getAll() {
        return wxGroupDao.getAll();
    }

    @Override
    public Pager<WxGroup> findPager(Pager<WxGroup> pager, List<PropertyFilter> list) {
        return wxGroupDao.findPager(pager, list);
    }

    @Override
    public WxGroup getGroup(Long id) {
        return wxGroupDao.get(id);
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            List<PropertyFilter> list = new ArrayList<PropertyFilter>();
            list.add(new PropertyFilter("EQS_group.id", id.toString()));
            Pager<UserInfo> pager = userInfoService.findPager(new Pager<UserInfo>(), list);
            for (UserInfo ui : pager.getPageItems()) {
                ui.setWxGroup(null);
                userInfoService.save(ui);
            }
            wxGroupDao.delete(id);
        }
    }

    @Override
    public int create(String name) {
        try {
            WxMpGroup res = config.getUtil().groupCreate(name);
            WxGroup wxGroup = new WxGroup(res.getId(), res.getName());
            wxGroup.setCount(res.getCount());
            wxGroupDao.save(wxGroup);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    @Override
    public int update(Long id, String name) {
        WxMpGroup wxMpGroup = new WxMpGroup();
        wxMpGroup.setId(id);
        wxMpGroup.setName(name);
        WxGroup wxGroup = new WxGroup(id, name);
        try {
            config.getUtil().groupUpdate(wxMpGroup);
            wxGroupDao.save(wxGroup);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    @Override
    public List<WxGroup> refreshGroup() {
        List<WxGroup> gl = new ArrayList<WxGroup>();
        try {
            List<WxMpGroup> list = config.getUtil().groupGet();
            for (WxMpGroup g : list) {
                WxGroup wxGroup = BeanUtil.copyProperties(new WxGroup(), g);
                wxGroupDao.save(wxGroup);
                gl.add(wxGroup);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return gl;
    }

    @Override
    public Long getUserGroup(String openId) throws WxException {
        try{
            return config.getUtil().userGetGroup(openId);
        }catch (WxErrorException e){
            throw new WxException(WxErrorInfo.wxExceptionBuilder(e.getError()));
        }
    }

    @Override
    public int moveGroup(String openId, Long groupId) {
        try {
            config.getUtil().userUpdateGroup(openId, groupId);
            UserInfo ui = new UserInfo();
            ui.setOpenId(openId);
            ui.setWxGroup(new WxGroup(groupId, null));
            userInfoService.save(ui);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

}
