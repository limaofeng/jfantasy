package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.Group;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository("wx.GroupDao")
public class GroupDao extends HibernateDao<Group, Long> {
}
