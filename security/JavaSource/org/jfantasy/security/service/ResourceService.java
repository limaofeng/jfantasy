package org.jfantasy.security.service;

import org.hibernate.criterion.Criterion;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Resource;
import org.jfantasy.security.dao.ResourceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourceService {

    @javax.annotation.Resource
    private ResourceDao resourceDao;

    /**
     * 搜索
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return {Pager}
     */
    public Pager<Resource> findPager(Pager<Resource> pager, List<PropertyFilter> filters) {
        return this.resourceDao.findPager(pager, filters);
    }

    public Resource save(Resource resource) {
        return this.resourceDao.save(resource);
    }

    public Resource findUnique(Criterion... criterions) {
        return this.resourceDao.findUnique(criterions);
    }

    public Resource get(Long id) {
        return this.resourceDao.get(id);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.resourceDao.delete(id);
        }
    }

    public List<Resource> find(Criterion... criterions) {
        return this.resourceDao.find(criterions);
    }
}