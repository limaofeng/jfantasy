package com.fantasy.wx.config.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.config.bean.WeixinConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class WeixinConfigDao extends HibernateDao<WeixinConfig, String> {
}
