package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import java.util.List;

/**
 * 组织机构关系
 */
public class OrgRelation extends BaseBusEntity {

    public static enum Type {
        /**
         * 下级(普通下属关系)
         */
        underling,
        /**
         * 分支机构(用于表示子公司,可承担独立法律责任组织)
         */
        branch
    }

    /**
     * 标示主键
     */
    private Long id;
    /**
     * 组织机构
     */
    private Organization organization;
    /**
     * 下级组织机构
     */
    private List<Organization> children;

    private Type type;

    /**
     * 机构关系对应的维度
     */
    private OrgDimension orgDimension;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Organization> getChildren() {
        return children;
    }

    public void setChildren(List<Organization> children) {
        this.children = children;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public OrgDimension getOrgDimension() {
        return orgDimension;
    }

    public void setOrgDimension(OrgDimension orgDimension) {
        this.orgDimension = orgDimension;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
