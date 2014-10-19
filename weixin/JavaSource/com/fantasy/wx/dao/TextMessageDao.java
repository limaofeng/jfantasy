package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.req.TextMessage;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class TextMessageDao extends HibernateDao<TextMessage, Long> {

}
