package org.jfantasy.wx.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.bean.UserKey;
import org.springframework.stereotype.Repository;

@Repository("wx.userDao")
public class UserDao extends HibernateDao<User, UserKey> {
}
