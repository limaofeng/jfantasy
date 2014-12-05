package com.fantasy.wx.user.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.user.bean.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class UserInfoDao extends HibernateDao<UserInfo, String> {
}
