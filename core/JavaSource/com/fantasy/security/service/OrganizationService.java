package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.OrgHelpBean;
import com.fantasy.security.bean.OrgRelation;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.dao.OrgRelationDao;
import com.fantasy.security.dao.OrganizationDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhx on 2015/1/21.
 */
@Service
@Transactional
public class OrganizationService {

    @Resource
    private OrganizationDao organizationDao;

    @Resource
    private OrgRelationDao orgRelationDao;


    public Pager<Organization> findPager(Pager<Organization> pager, List<PropertyFilter> filters) {
        return this.organizationDao.findPager(pager, filters);
    }


    public List<Organization> find(List<PropertyFilter> filters) {
        //顶级组织机构
        List<OrgRelation> orgRelations = this.orgRelationDao.find(filters);

        //顶级组织机构
        List<Organization> organizations = new ArrayList<Organization>();
        for (OrgRelation relation : orgRelations) {
            //当前组织机构
            Organization organization = relation.getOrganization();
            organizations.add(organization);
            getRelation(relation);
        }
        //获取树桩组织机构
        return organizations;
    }

    private void getRelation(OrgRelation relation) {
        //当前组织机构
        Organization organization = relation.getOrganization();
        //下级组织机构
        List<OrgRelation> childrenRelation = this.orgRelationDao.find(Restrictions.eq("orgDimension.id", relation.getOrgDimension().getId()), Restrictions.eq("parent.id", relation.getId()));
        List<Organization> childrenAtion = new ArrayList<Organization>();
        for (OrgRelation childRelation : childrenRelation) {
            childrenAtion.add(childRelation.getOrganization());
            getRelation(childRelation);
        }
        organization.setChildren(childrenAtion);
    }


    public Organization save(Organization organization) {
        //维护关系
        List<OrgHelpBean> orgHelpBeans = organization.getOrgHelpBeans();
        //先保存当前组织机构
        this.organizationDao.save(organization);
        for (OrgHelpBean orgHelpBean : ObjectUtil.defaultValue(orgHelpBeans, new ArrayList<OrgHelpBean>())) {
            //当前层级关系
            OrgRelation newRelation = new OrgRelation();
            //上级组织
            Organization parent = orgHelpBean.getOrganization();
            if (parent != null) {//有上级组织
                //上级关系
                OrgRelation parentRelation = this.findUniqueRelation(Restrictions.eq("orgDimension.id", orgHelpBean.getOrgDimension().getId()), Restrictions.eq("organization.id", parent.getId()));
                //层级
                newRelation.setLayer(parentRelation.getLayer() + 1);
                //排序
                newRelation.setSort(parentRelation.getLayer() + 1);
                newRelation.setPath(parentRelation.getPath() + ","+newRelation.getId());
                newRelation.setParent(parentRelation);
                //组织维度
                newRelation.setOrgDimension(orgHelpBean.getOrgDimension());
                //当前组织机构
                newRelation.setOrganization(organization);
            } else {//没有上级组织
                //组织维度
                newRelation.setOrgDimension(orgHelpBean.getOrgDimension());
                //层级
                newRelation.setLayer(1);
                //排序
                newRelation.setSort(1);
                newRelation.setPath(organization.getId());
                //当前组织机构
                newRelation.setOrganization(organization);
            }
            this.orgRelationDao.save(newRelation);
        }
        return organization;
    }

    public OrgRelation findUniqueRelation(Criterion... criterions) {
        return this.orgRelationDao.findUnique(criterions);
    }


    public Organization findUnique(Criterion... criterions) {
        return this.organizationDao.findUnique(criterions);
    }

    public void delete(String... ids) {
        for (String id : ids) {
            List<OrgRelation> orgRelations = this.orgRelationDao.find(Restrictions.eq("organization.id", id));
            //组织机构关系
            for (OrgRelation orgRelation : orgRelations) {
                List<OrgRelation> children = orgRelation.getChildren();
                if (children == null || children.isEmpty()) {
                    this.orgRelationDao.delete(orgRelation);
                } else {
                    for (OrgRelation child : children) {
                        //改变上级归属
                        OrgRelation parent = orgRelation.getParent();
                        if (parent == null) {
                            child.setLayer(1);
                            child.setSort(1);
                            child.setParent(null);
                        } else {
                            child.setLayer(parent.getLayer() + 1);
                            child.setSort(parent.getSort() + 1);
                            child.setParent(parent);
                        }
                        this.orgRelationDao.save(child);
                    }
                    this.orgRelationDao.delete(orgRelation);
                }
            }
            this.organizationDao.delete(findUnique(Restrictions.eq("id", id)));
        }
    }


}
