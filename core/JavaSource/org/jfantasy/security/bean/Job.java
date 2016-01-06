package org.jfantasy.security.bean;

import org.jfantasy.attr.storage.BaseDynaBean;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 岗位
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:48
 */
@Entity
@Table(name = "AUTH_JOB")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job extends BaseDynaBean {

    private static final long serialVersionUID = -7020427994563623645L;

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 岗位编码
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 岗位名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 岗位描述信息
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     *组织机构
     */
    @ManyToOne
    @JoinColumn(name="ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_JOB_ORGANIZATION"))
    private Organization organization;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
