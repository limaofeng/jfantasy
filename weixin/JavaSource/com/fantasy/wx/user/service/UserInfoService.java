package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.user.dao.UserInfoDao;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
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
public class UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private WeixinConfigInit config;

    /**
     * 获取所有未添加的微信粉丝，并刷新粉丝状态
     * @param openId
     * @return
     */
    public List<String> getRefreshOpenId(List<String> openId){
        userInfoDao.batchSQLExecute("UPDATE wx_user_info SET subscribe=false");
        UserInfo u=null;
        List<String> sList=new ArrayList<String>();
        for(String s:openId){
            u= userInfoDao.get(s);
            if (u == null) {
                sList.add(s);
            } else {
                u.setSubscribe(true);
                userInfoDao.save(u);
            }
        }
        return sList;
    }

    /**
     * 保存多个用户
     * @param uArray
     */
    public void saveArry(UserInfo[] uArray){
        for(UserInfo u:uArray){
            userInfoDao.save(u);
        }
    }

    /**
     * 获取
     * @param openId
     * @return
     */
    public UserInfo getUserInfo(String openId){
        return userInfoDao.get(openId);
    }

    /**
     * 保存
     * @param ui
     */
    public void save(UserInfo ui){
        userInfoDao.save(ui);
    }
    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<UserInfo> findPager(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        return this.userInfoDao.findPager(pager, filters);
    }

    /**
     * 刷新粉丝列表
     */
    public void refresh() throws WxErrorException {
        WxMpService service=config.getUtil();
        WxMpUserList userList=null;
        List<String> refreshList=new ArrayList<String>();
        do{
            userList=service.userList(userList==null?null:userList.getNextOpenId());
            refreshList.addAll(userList.getOpenIds());
        }while(userList!=null&&userList.getTotal()>userList.getCount());

        for(String s:getRefreshOpenId(refreshList)){
            UserInfo ui= BeanUtil.copyProperties(new UserInfo(), service.userInfo(s, null));
            userInfoDao.save(ui);
        }
    }

    public void delete(String openId){
        userInfoDao.delete(openId);
    }

    /**
     * 查询所有用户未读的消息数
     * @param list
     */
    public void countUnReadSize(List<UserInfo> list){
        for(UserInfo u:list){
            setUnReadSize(u);
        }
    }

    /**
     * 获取某个用户的未读消息数
     * @param u
     */
    public void setUnReadSize(UserInfo u){
        List query=userInfoDao.createSQLQuery("SELECT COUNT(*) c FROM wx_message WHERE create_time>? and openid=?", u.getLastLookTime(),u.getOpenId()).list();
        if(query.size()!=0){
            u.setUnReadSize(Integer.parseInt(query.get(0).toString()));
        }
    }

    /**
     * 刷新用户最后查看消息时间
     * @param ui
     */
    public void refreshMessage(UserInfo ui){
        userInfoDao.batchSQLExecute("update wx_user_info set last_look_time=last_message_time  where openid=?",ui.getOpenId());
        ui.setLastLookTime(ui.getLastMessageTime());
    }



}
