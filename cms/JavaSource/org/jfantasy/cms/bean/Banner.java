package org.jfantasy.cms.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * 横幅图维护
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-3-3 上午11:22:45
 */
@Entity
@Table(name = "CMS_BANNER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Banner extends BaseBusEntity {

    private static final long serialVersionUID = -5628208241667269397L;
    /**
     * 唯一编码
     */
    @Id
    @Column(name = "CODE", length = 20, unique = true, nullable = false)
    private String key;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 上传图片大小
     */
    @Column(name = "SIZE", length = 20)
    private String size;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    /**
     * 对应的明细项
     */
    @OneToMany(mappedBy = "banner", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<BannerItem> bannerItems;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<BannerItem> getBannerItems() {
        return bannerItems;
    }

    public void setBannerItems(List<BannerItem> bannerItems) {
        this.bannerItems = bannerItems;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", bannerItems=" + bannerItems +
                '}';
    }
}
