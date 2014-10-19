package com.fantasy.cms.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 专题表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午06:39:55
 */
@Entity
@Table(name = "CMS_SPECIAL")
public class Special extends BaseBusEntity {

    private static final long serialVersionUID = -5097339037069263955L;

    /**
     * 专题编码
     */
    @Id
    @Column(name = "CODE", length = 50, nullable = false, insertable = true, updatable = false)
    private String code;
    /**
     * 专题名称
     */
    private String name;
    /**
     * 期刊号
     */
    private String issn;
    /**
     * 专题对应的URL
     */
    private String url;
    /**
     * 发布日期
     */
    private Date releaseDate;
    /**
     * 专题对应的栏目
     */
    private ArticleCategory category;
    /**
     * 描述
     */
    private String description;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleCategory getCategory() {
        return category;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}
