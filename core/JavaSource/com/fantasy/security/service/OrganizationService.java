package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.dao.OrgDimensionDao;
import com.fantasy.security.dao.OrganizationDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2015/1/21.
 */
@Service
@Transactional
public class OrganizationService {

    @Resource
    private OrganizationDao organizationDao;


    public Pager<Organization> findPager(Pager<Organization> pager,List<PropertyFilter> filters){
        return  this.organizationDao.findPager(pager, filters);
    }

    public Organization save(Organization organization){
        this.organizationDao.save(organization);
        return organization;
    }

    public Organization findUnique(Criterion...criterions){
       return this.organizationDao.findUnique(criterions);
    }

    public void delete(String...ids){
        for(String id:ids){
            this.organizationDao.delete(this.findUnique(Restrictions.eq("id",id)));
        }
    }


}
