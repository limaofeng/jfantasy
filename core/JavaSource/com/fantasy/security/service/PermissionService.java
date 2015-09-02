package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.Permission;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.bean.enums.ResourceType;
import com.fantasy.security.dao.PermissionDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    public Pager<Permission> findPager(Pager<Permission> pager, List<PropertyFilter> filters) {
        return this.permissionDao.findPager(pager, filters);
    }

    public Permission save(Permission permission) {
        return this.permissionDao.save(permission);
    }

    public Permission get(Long id) {
        return this.permissionDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.permissionDao.delete(id);
        }
    }

    @Transactional(readOnly = true)
    public Map<Resource, List<Permission>> loadPermissions() {
        Map<Resource, List<Permission>> urlAuthorities = new HashMap<Resource, List<Permission>>();
        for (Permission permission : loadPermissionByUrl()) {
            Resource resource = permission.getResource();
            if (!resource.isEnabled()) {
                continue;
            }
            if (!permission.isEnabled()) {
                continue;
            }
            if (urlAuthorities.containsKey(resource)) {
                urlAuthorities.put(resource, new ArrayList<Permission>());
            }
            urlAuthorities.get(resource).add(permission);
        }
        return urlAuthorities;
    }

    private List<Permission> loadPermissionByUrl() {
        return this.permissionDao.find(Restrictions.eq("resource.type", ResourceType.url));
    }

    public List<Permission> find(Criterion... criterions) {
        return this.permissionDao.find(criterions);
    }
}
