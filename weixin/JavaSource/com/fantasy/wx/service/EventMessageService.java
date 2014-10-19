package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.req.EventMessage;
import com.fantasy.wx.dao.EventMessageDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class EventMessageService {
    @Resource
    private EventMessageDao eventMessageDao;

    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<EventMessage> findPager(Pager<EventMessage> pager, List<PropertyFilter> filters) {
        return this.eventMessageDao.findPager(pager, filters);
    }

    public EventMessage save(EventMessage textMessage){
        eventMessageDao.save(textMessage);
        return textMessage;
    }


}
