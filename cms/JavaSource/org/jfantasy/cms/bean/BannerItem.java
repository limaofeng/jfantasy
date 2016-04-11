package org.jfantasy.cms.bean;

import org.jfantasy.filestore.bean.FileDetail;
import org.jfantasy.filestore.bean.converter.FileDetailConverter;
import org.jfantasy.filestore.bean.databind.FileDetailDeserializer;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 横幅图维护项
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-3-4 下午12:56:46
 */
@Entity
@Table(name = "CMS_BANNER_ITEM")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "banner"})
public class BannerItem extends BaseBusEntity {

    private static final long serialVersionUID = 3179187068898470124L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 标题
     */
    @Column(name = "TITLE", length = 200)
    private String title;
    /**
     * 摘要
     */
    @Column(name = "SUMMARY", length = 500)
    private String summary;
    /**
     * 跳转地址
     */
    @Column(name = "URL", length = 100)
    private String url;
    /**
     * 图片存储位置
     */
    @Column(name = "BANNER_IMAGE_STORE", length = 500)
    @Convert(converter = FileDetailConverter.class)
    @JsonDeserialize(using = FileDetailDeserializer.class)
    private FileDetail bannerImage;
    /**
     * 排序字段
     */
    @Column(name = "SORT")
    private Integer sort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANNER_CODE")
    private Banner banner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBannerImage(FileDetail bannerImage) {
        this.bannerImage = bannerImage;
    }

    public FileDetail getBannerImage() {
        return this.bannerImage;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "BannerItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                ", bannerImage='" + bannerImage + '\'' +
                '}';
    }
}
