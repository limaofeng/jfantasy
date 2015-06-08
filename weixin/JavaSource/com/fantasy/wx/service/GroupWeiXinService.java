package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.account.Consts;
import com.fantasy.wx.bean.Group;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.dao.GroupDao;
import com.fantasy.wx.dao.UserInfoDao;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.session.WeiXinSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class GroupWeiXinService implements InitializingBean {
    @Autowired
    private WeiXinSessionFactory factory;

    private WeiXinSession session;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        session = Consts.appid != null ? factory.openSession(Consts.appid) : null;
    }

    public Group save(Group group) {
        groupDao.save(group);
        return group;
    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public Pager<Group> findPager(Pager<Group> pager, List<PropertyFilter> list) {
        return groupDao.findPager(pager, list);
    }

    public Group getGroup(Long id) {
        return groupDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            userInfoDao.batchSQLExecute("update wx_user_info set GROUP_ID=null where GROUP_ID=?", id);
            groupDao.delete(id);
        }
    }

    public int create(String name) {
        com.fantasy.wx.framework.message.user.Group res = session.createGroup(name);
        Group group = new Group(res.getId(), res.getName(), res.getCount());
        group.setCount(res.getCount());
        groupDao.save(group);
        return 0;
    }

    public int update(Long id, String name) {
        Group group = new Group(id, name);
        session.updateGroup(id, name);
        groupDao.save(group);
        return 0;
    }

    public List<Group> refreshGroup() {
        List<Group> gl = new ArrayList<Group>();
        List<com.fantasy.wx.framework.message.user.Group> list = session.getGroups();
        for (com.fantasy.wx.framework.message.user.Group g : list) {
            Group group = new Group(g.getId(), g.getName(), g.getCount());
            groupDao.save(group);
            gl.add(group);
        }
        return gl;
    }

    public int moveGroup(String openId, Long groupId) {
        session.moveUser(openId, groupId);
        UserInfo ui = new UserInfo();
        ui.setOpenId(openId);
        ui.setGroup(new Group(groupId, null));
        userInfoDao.save(ui);
        return 0;
    }
}
