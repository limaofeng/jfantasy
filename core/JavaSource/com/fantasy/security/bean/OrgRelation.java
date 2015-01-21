package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 组织机构关系
 */
@Entity
@Table(name = "AUTH_ORG_RELATION")
public class OrgRelation extends BaseBusEntity {

    public static enum Type {
        /**
         * 下级(普通下属关系)
         */
        underling,
        /**
         * 分支机构(用于表示子公司,可承担独立法律责任组织)
         */
        branch,
        /**
         * 子公司
         */
        subsidiary
    }

    /**
     * 标示主键
     */
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 组织机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ORG_RELATION"))
    private Organization organization;
    /**
     * 下级组织机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSIDIARY_ID", foreignKey = @ForeignKey(name = "FK_AUTH_SUB_ORG_RELATION"))
    private Organization subsidiary;
    /**
     * 机构关系对应的维度
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_DIMENSION_ID", foreignKey = @ForeignKey(name = "FK_AUTH_ORG_DIMENSION"))
    private OrgDimension orgDimension;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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

    public Organization getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(Organization subsidiary) {
        this.subsidiary = subsidiary;
    }
}
