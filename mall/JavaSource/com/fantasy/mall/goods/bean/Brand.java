package com.fantasy.mall.goods.bean;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.jackson.JSON;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 品牌
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午5:46:16
 */
@Entity
@Table(name = "MALL_BRAND")
@JsonIgnoreProperties({"hibernateLazyInitializer", "introduction", "logoImageStore", "goods", "goodsCategories"})
public class Brand extends BaseBusEntity {

    private static final long serialVersionUID = -6109590619136943215L;
    @Id
    @Column(name = "id", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", nullable = false)
    private String name;
    /**
     * 商品英文名称
     */
    @Column(name = "ENG_NAME", length = 100)
    private String engname;
    /**
     * 图片存储位置
     */
    @Column(name = "LOGO_IMAGE_STORE", length = 500)
    private String logoImageStore;
    /**
     * 网址
     */
    @Column(name = "URL", length = 150)
    private String url;
    /**
     * 介绍
     */
    @Column(name = "INTRODUCTION", length = 3000)
    private String introduction;
    /**
     * 所属国家
     */
    @Column(name = "NATION", length = 20)
    private String nation;
    /**
     * 品牌对应的商品
     */
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Goods> goods;
    /**
     * 商品分类
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MALL_GOODS_CATEGORY_BRAND", joinColumns = @JoinColumn(name = "BRAND_ID"), inverseJoinColumns = @JoinColumn(name = "GOODS_CATEGORY_ID"), foreignKey = @ForeignKey(name = "FK_BRAND_GOODS_CATEGORIE"))
    private List<GoodsCategory> goodsCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public List<GoodsCategory> getGoodsCategories() {
        return goodsCategories;
    }

    public void setGoodsCategories(List<GoodsCategory> goodsCategories) {
        this.goodsCategories = goodsCategories;
    }

    public String getEngname() {
        return engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getLogoImageStore() {
        return logoImageStore;
    }

    @TypeConversion(key = "logoImageStore", converter = "com.fantasy.file.bean.converter.FileDetailStoreConverter")
    public void setLogoImageStore(String logoImageStore) {
        this.logoImageStore = logoImageStore;
    }

    /**
     * 获取品牌 Logo
     *
     * @return {FileDetail}
     */
    public FileDetail getLogoImage() {
        if (StringUtils.isEmpty(this.logoImageStore)) {
            return null;
        }
        List<FileDetail> fileDetails = JSON.deserialize(this.logoImageStore, new TypeReference<List<FileDetail>>() {
        });
        return fileDetails.isEmpty() ? null : fileDetails.get(0);
    }
}