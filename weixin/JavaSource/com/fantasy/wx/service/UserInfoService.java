package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.pojo.WatchUserList;
import com.fantasy.wx.dao.UserInfoDao;
import com.fantasy.wx.util.WeixinUtil;
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

    public String[] checkOpenId(String[] openId){
        userInfoDao.batchSQLExecute("UPDATE wx_user_info SET subscribe='0'");
        UserInfo u=null;
        List<String> sList=new ArrayList<String>();
        for(String s:openId){
            u= userInfoDao.get(s);
            if (u == null) {
                sList.add(s);
            } else {
                u.setSubscribe(1);
                userInfoDao.save(u);
            }
        }
        String[] sarry=new String[sList.size()];
        sList.toArray(sarry);
        return sarry;
    }
    public void saveArry(UserInfo[] uArray){
        for(UserInfo u:uArray){
            userInfoDao.save(u);
        }
    }
    public UserInfo getUserInfo(String openId){
        return userInfoDao.get(openId);
    }

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
    public void refresh(String appid){
        WeixinUtil wxUtil=new WeixinUtil();
        AccessToken at=WeixinUtil.accessToken.get(appid);
        WatchUserList watchUserList=wxUtil.getWatchUserList(at);
        String[] arry=checkOpenId(watchUserList.getData().getOpenid());
        for(int i=0;i<arry.length;i++){
            UserInfo ui=wxUtil.getUserInfo(arry[i],at);
            userInfoDao.save(ui);
        }
    }

}
