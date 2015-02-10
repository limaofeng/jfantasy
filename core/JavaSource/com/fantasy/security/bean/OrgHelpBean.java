package com.fantasy.security.bean;

/**
 * Created by yhx on 2015/1/22.
 *
 * 组织机构 帮助表
 *
 */
public class OrgHelpBean {


    /**
     * 组织维度
     */
    private OrgDimension orgDimension;

    /**
     * 上级组织机构
     */
    private Organization organization;


    public OrgDimension getOrgDimension() {
        return orgDimension;
    }

    public void setOrgDimension(OrgDimension orgDimension) {
        this.orgDimension = orgDimension;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
