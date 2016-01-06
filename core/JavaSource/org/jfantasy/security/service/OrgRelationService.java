package org.jfantasy.security.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.OrgRelation;
import org.jfantasy.security.dao.OrgRelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hebo on 2015/4/1.
 * 维度与组织机构中间关系
 */
@Service
@Transactional
public class OrgRelationService {


    @Autowired
    private OrgRelationDao orgRelationDao;


    public Pager<OrgRelation> findPager(Pager<OrgRelation> pager,List<PropertyFilter> filters){
        return this.orgRelationDao.findPager(pager,filters);
    }

}
