package com.fantasy.cms.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

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
public class Banner extends BaseBusEntity {

    private static final long serialVersionUID = -5628208241667269397L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 唯一编码
     */
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
    private List<BannerItem> bannerItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}
