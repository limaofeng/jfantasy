package com.fantasy.security.service;

import com.fantasy.security.bean.Organization;
import com.fantasy.security.dao.OrganizationDao;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hebo on 2015/1/6.
 * 组织机构
 */
@Service
@Transactional
public class OrganizationService {

    @Resource
    private OrganizationDao organizationDao;

    /**
     * 查询所有组织机构  按创建时间升序排序
     * @return List<Organization>
     */
    public List<Organization> getAll(){
        return this.organizationDao.find(new Criterion[0],"createTime","asc");
    }

    /**
     * 根据
     * @param id   组织机构编码 id
     * @return  Organization
     */
    public Organization get(String id){
        return this.organizationDao.get(id);
    }



}
