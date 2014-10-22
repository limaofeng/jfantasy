package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.bean.pojo.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class AccessTokenDao extends HibernateDao<AccessToken, Long> {
}