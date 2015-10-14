package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.User;
import com.fantasy.wx.bean.UserKey;
import org.springframework.stereotype.Repository;

@Repository("wx.userDao")
public class UserDao extends HibernateDao<User, UserKey> {
}
