package com.fantasy.security.service;

import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.OrgRelation;
import com.fantasy.security.dao.OrgDimensionDao;
import com.fantasy.security.dao.OrgRelationDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by yhx on 2015/1/21.
 */
@Service
@Transactional
public class OrgDimensionService {

    @Autowired
    private OrgDimensionDao orgDimensionDao;

    @Autowired
    private OrgRelationDao orgRelationDao;


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
            //先删除关系
            List<OrgRelation> relations = this.orgRelationDao.find(Restrictions.eq("orgDimension.id",id));
            for (OrgRelation relation : relations) {
                this.orgRelationDao.delete(relation);
            }
            //再删除组织维度
            OrgDimension orgDimension = this.findUnique(Restrictions.eq("id",id));
            this.orgDimensionDao.delete(orgDimension);
        }
    }


}
