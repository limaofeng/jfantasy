package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Role;
import com.fantasy.security.dao.RoleDao;
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