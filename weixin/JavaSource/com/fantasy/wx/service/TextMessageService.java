package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.pojo.WatchUserList;
import com.fantasy.wx.bean.req.TextMessage;
import com.fantasy.wx.dao.TextMessageDao;
import com.fantasy.wx.dao.UserInfoDao;
import com.fantasy.wx.util.WeixinUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class TextMessageService {
    @Resource
    private TextMessageDao textMessageDao;

    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<TextMessage> findPager(Pager<TextMessage> pager, List<PropertyFilter> filters) {
        return this.textMessageDao.findPager(pager, filters);
    }

    public TextMessage save(TextMessage textMessage){
        textMessageDao.save(textMessage);
        return textMessage;
    }


}
