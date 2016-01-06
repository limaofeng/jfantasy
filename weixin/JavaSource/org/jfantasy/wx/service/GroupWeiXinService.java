package org.jfantasy.wx.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.wx.bean.Group;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.dao.GroupDao;
import org.jfantasy.wx.dao.UserDao;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
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
public class GroupWeiXinService {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

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
            userDao.batchSQLExecute("update wx_user_info set GROUP_ID=null where GROUP_ID=?", id);
            groupDao.delete(id);
        }
    }

    public int create(String name) throws WeiXinException {
        org.jfantasy.wx.framework.message.user.Group res = WeiXinSessionUtils.getCurrentSession().createGroup(name);
        Group group = new Group(res.getId(), res.getName(), res.getCount());
        group.setCount(res.getCount());
        groupDao.save(group);
        return 0;
    }

    public int update(Long id, String name) throws WeiXinException {
        Group group = new Group(id, name);
        WeiXinSessionUtils.getCurrentSession().updateGroup(id, name);
        groupDao.save(group);
        return 0;
    }

    public List<Group> refreshGroup() throws WeiXinException {
        List<Group> gl = new ArrayList<Group>();
        List<org.jfantasy.wx.framework.message.user.Group> list = WeiXinSessionUtils.getCurrentSession().getGroups();
        for (org.jfantasy.wx.framework.message.user.Group g : list) {
            Group group = new Group(g.getId(), g.getName(), g.getCount());
            groupDao.save(group);
            gl.add(group);
        }
        return gl;
    }

    public int moveGroup(String openId, Long groupId) throws WeiXinException {
        WeiXinSessionUtils.getCurrentSession().moveUser(openId, groupId);
        User ui = new User();
        ui.setOpenId(openId);
        ui.setGroup(new Group(groupId, null));
        userDao.save(ui);
        return 0;
    }
}
