package org.jfantasy.security.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Permission;
import org.jfantasy.security.bean.Resource;
import org.jfantasy.security.bean.enums.ResourceType;
import org.jfantasy.security.dao.PermissionDao;
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
