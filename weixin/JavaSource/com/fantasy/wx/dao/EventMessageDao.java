package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.req.EventMessage;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/9/26.
 */
@Repository
public class EventMessageDao extends HibernateDao<EventMessage, Long> {

}
