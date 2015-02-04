package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.OrgPosition;
import com.fantasy.security.dao.OrgPositionDao;
import com.fantasy.swp.bean.Page;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2015/2/3.
 */
@Service
@Transactional
public class OrgPositionService {

    @Resource
    private OrgPositionDao orgPositionDao;


    public Pager<OrgPosition> findPager(Pager<OrgPosition> pager,List<PropertyFilter> filters){
        return this.orgPositionDao.findPager(pager,filters);
    }

    public void save(OrgPosition orgPosition){
        this.orgPositionDao.save(orgPosition);
    }

    public OrgPosition get(String id){
        return this.orgPositionDao.findUnique(Restrictions.eq("id", id));
    }

    public void delete(String id){
        this.orgPositionDao.delete(this.get(id));
    }

}
