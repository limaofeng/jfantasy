package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.user.bean.WxGroup;
import com.fantasy.wx.user.dao.WxGroupDao;
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
public class WxGroupService {
    @Resource
    private WxGroupDao wxGroupDao;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private WeixinConfigInit config;

    public WxGroup save(WxGroup wxGroup){
        wxGroupDao.save(wxGroup);
        return wxGroup;
    }
    public List<WxGroup> getAll(){
        return wxGroupDao.getAll();
    }

    public Pager<WxGroup> findPager(Pager<WxGroup> pager,List<PropertyFilter> list){
        return wxGroupDao.findPager(pager,list);
    }

    public WxGroup getGroup(Long id){
        return wxGroupDao.get(id);
    }

    public void delete(Long... ids){
        for(Long id:ids){
            List<PropertyFilter> list=new ArrayList<PropertyFilter>();
            list.add(new PropertyFilter("EQS_group.id",id.toString()));
            Pager<UserInfo> pager=userInfoService.findPager(new Pager<UserInfo>(),list);
            for(UserInfo ui:pager.getPageItems()){
                ui.setWxGroup(null);
                userInfoService.save(ui);
            }
            wxGroupDao.delete(id);
        }
    }

    /**
     * 创建分组
     * @param name
     * @return
     */
    public int create(String name){
        try {
            WxMpGroup res = config.getUtil().groupCreate(name);
            WxGroup wxGroup =new WxGroup(res.getId(),res.getName());
            wxGroup.setCount(res.getCount());
            wxGroupDao.save(wxGroup);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    /**
     * 更新分组名称
     * @param id
     * @param name
     * @return
     */
    public int update(Long id,String name){
        WxMpGroup wxMpGroup=new WxMpGroup();
        wxMpGroup.setId(id);
        wxMpGroup.setName(name);
        WxGroup wxGroup =new WxGroup(id,name);
        try {
            config.getUtil().groupUpdate(wxMpGroup);
            wxGroupDao.save(wxGroup);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    /**
     * 刷新用户分组
     * @return
     */
    public List<WxGroup> refreshGroup(){
        List<WxGroup> gl=new ArrayList<WxGroup>();
        try {
            List<WxMpGroup> list=config.getUtil().groupGet();
            for(WxMpGroup g:list){
                WxGroup wxGroup =BeanUtil.copyProperties(new WxGroup(),g);
                wxGroupDao.save(wxGroup);
                gl.add(wxGroup);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return gl;
    }
    /**
     * 获取用户所在分组的groupId
     * @param openId 用户id
     * @return groupid
     * @throws WxErrorException
     */
    public Long getUserGroup(String openId) throws WxErrorException {
        return config.getUtil().userGetGroup(openId);
    }

    /**
     * 移动分组
     * @param openId 用户id
     * @param groupId 分组id
     * @return
     */
    public int moveGroup(String openId,Long groupId){
        try {
            config.getUtil().userUpdateGroup(openId,groupId);
            UserInfo ui=new UserInfo();
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
