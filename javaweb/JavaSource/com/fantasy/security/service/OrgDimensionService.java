package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.dao.OrgDimensionDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hebo on 2015/1/6.
 * 维度管理Service
 */
@Service
@Transactional
public class OrgDimensionService {

    @Resource
    private OrgDimensionDao orgDimensionDao;


    /**
     * 分页查询
     * @param pager  分页对象
     * @param filters  过滤条件
     * @return  Pager<OrgDimension>
     */
    public Pager<OrgDimension> findPager(Pager<OrgDimension> pager, List<PropertyFilter> filters){
        return this.orgDimensionDao.findPager(pager,filters);
    }

    /**
     * 保存
     * @param orgDimension  传入的对象
     * @return OrgDimension
     */
    public OrgDimension save(OrgDimension orgDimension){
        this.orgDimensionDao.save(orgDimension);
        return orgDimension;
    }

    /**
     * 根据ID查询对象
     * @param id ID
     * @return OrgDimension
     */
    public OrgDimension get(String id){
        return this.orgDimensionDao.get(id);
    }

    /**
     * 删除方法
     * @param ids ID数组
     */
    public void delete(String... ids){
        for(String id:ids){
            this.orgDimensionDao.delete(id);
        }
    }
}
