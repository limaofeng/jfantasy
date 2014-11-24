package com.fantasy.wx.menu.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.menu.bean.WxMenu;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class WxMenuDao extends HibernateDao<WxMenu, Long> {
}
