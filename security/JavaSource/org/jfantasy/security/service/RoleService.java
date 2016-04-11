package org.jfantasy.security.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public List<Role> getAll() {
        return roleDao.findBy("enabled", true);
    }

    public Pager<Role> findPager(Pager<Role> pager, List<PropertyFilter> filters) {
        return this.roleDao.findPager(pager, filters);
    }

    public Role save(Role role) {
        return roleDao.save(role);
    }

    public Role get(String id) {
        return this.roleDao.get(id);
    }

    public void delete(String... ids) {
        for (String code : ids) {
            this.roleDao.delete(code);
        }
    }

}