package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.dao.OrgDimensionDao;
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
public class OrgDimensionService {

    @Resource
    private OrgDimensionDao orgDimensionDao;


   public List<OrgDimension> find(Criterion...criterions){
      return this.orgDimensionDao.find(criterions);
   }

    public OrgDimension findUnique(Criterion...criterions){
       return this.orgDimensionDao.findUnique(criterions);
    }

    public OrgDimension save(OrgDimension orgDimension){
        this.orgDimensionDao.save(orgDimension);
        return orgDimension;
    }

    public void delete(String...ids){
        for(String id:ids){
            OrgDimension orgDimension = this.findUnique(Restrictions.eq("id",id));
            this.orgDimensionDao.delete(orgDimension);
        }
    }


}
