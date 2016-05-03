package org.jfantasy.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 组织机构关系
 */
@Entity
@Table(name = "AUTH_ORG_RELATION", uniqueConstraints = {@UniqueConstraint(columnNames = {"ORG_DIMENSION_ID", "ORG_ID"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parent","children"})
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
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 用于存储关系path
     */
    @Column(name = "PATH", nullable = false, length = 3000)
    private String path;
    /**
     * 上级关系
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_AUTH_ORG_PID"))
    private OrgRelation parent;
    /**
     * 下级关系
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    private List<OrgRelation> children;
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

    public OrgRelation getParent() {
        return parent;
    }

    public void setParent(OrgRelation parent) {
        this.parent = parent;
    }

    public List<OrgRelation> getChildren() {
        return children;
    }

    public void setChildren(List<OrgRelation> children) {
        this.children = children;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
