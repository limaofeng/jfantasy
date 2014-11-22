package com.fantasy.wx.menu.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.config.bean.WeixinConfig;
import com.fantasy.wx.menu.bean.Menu;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class MenuDao extends HibernateDao<Menu, Long> {
}
