package com.fantasy.mall.goods.bean;

import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.attr.storage.BaseDynaBean;
import com.fantasy.framework.lucene.annotations.Compare;
import com.fantasy.framework.lucene.annotations.IndexFilter;
import com.fantasy.framework.lucene.annotations.Indexed;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.shop.bean.Shop;
import com.fantasy.member.bean.Member;
import com.fantasy.system.util.SettingUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:19:17
 */
@Indexed
@Entity
@Table(name = "MALL_GOODS")
@Persister(impl = DynaBeanEntityPersister.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "introduction", "metaKeywords", "metaDescription", "favoriteMembers", "comments", "products", "goodsImageStore", "goodsParameterValueStore"})
public class Goods extends BaseDynaBean {

    private static final long serialVersionUID = 7710250000511514557L;

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 商品编号
     */
    @Column(name = "SN", nullable = false, unique = true, updatable = false)
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('GOODS-SN'), 5)")})
    private String sn;
    /**
     * 商品名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 商品英文名称
     */
    @Column(name = "ENG_NAME", length = 100)
    private String engname;
    /**
     * 销售价
     */
    @Column(name = "PRICE", length = 10, scale = 2, nullable = false)
    private BigDecimal price;
    /**
     * 成本价
     */
    @Column(name = "COST", precision = 15, scale = 5)
    private BigDecimal cost;
    /**
     * 市场价(指导价格)
     */
    @Column(name = "MARKET_PRICE", length = 10, scale = 2, nullable = false)
    private BigDecimal marketPrice;
    /**
     * 商品重量(单位: 克)
     */
    @Column(name = "WEIGHT")
    private Integer weight;
    /**
     * 库存
     */
    @Column(name = "STORE")
    private Integer store;
    /**
     * 被占用库存数
     */
    @Column(name = "FREEZE_STORE")
    private Integer freezeStore;
    /**
     * 月销量(近30天的)
     */
    @Column(name = "MONTH_SALE_COUNT", nullable = false)
    private Integer monthSaleCount;
    /**
     * 销量(总)
     */
    @Column(name = "SALE_COUNT", nullable = false)
    private Integer saleCount;
    /**
     * 积分
     */
    @Column(name = "SCORE")
    private Integer score;
    /**
     * 是否上架
     */
    @IndexFilter(compare = Compare.IS_EQUALS, value = "true")
    @Column(name = "MARKETABLE", nullable = false)
    private Boolean marketable;
    /**
     * 产品介绍(基本概况)
     */
    @Lob
    @Column(name = "INTRODUCTION")
    private String introduction;
    /**
     * 页面关键词
     */
    @Column(name = "META_KEYWORDS", length = 1000)
    private String metaKeywords;
    /**
     * 页面描述
     */
    @Column(name = "META_DESCRIPTION", length = 1000)
    private String metaDescription;
    /**
     * 商品图片存储
     */
    @Column(name = "GOODS_IMAGE_STORE", length = 3000)
    private String goodsImageStore;
    /**
     * 是否启用商品规格
     */
    @Column(name = "SPECIFICATION_ENABLED", nullable = false)
    private Boolean specificationEnabled;
    /**
     * 品牌
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID", foreignKey = @ForeignKey(name = "FK_GOODS_BRAND"))
    private Brand brand;
    /**
     * 收藏
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MALL_FAVORITE_MEMBER", joinColumns = @JoinColumn(name = "GOODS_ID"), inverseJoinColumns = @JoinColumn(name = "MEMBER_ID"), foreignKey = @ForeignKey(name = "FK_MEMBER_FAVORITE_GOODS"))
    private List<Member> favoriteMembers;
    /**
     * 货品
     */
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Product> products;
    /**
     * 商品所属分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_GOODS_CATEGORY"))
    private GoodsCategory category;
    /**
     * 商品规格
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MALL_GOODS_SPECIFICATION", joinColumns = @JoinColumn(name = "GOODS_ID"), inverseJoinColumns = @JoinColumn(name = "SPECIFICATION_ID"), foreignKey = @ForeignKey(name = "FK_GOODS_SPECIFICATION"))
    @OrderBy("sort asc")
    private List<Specification> specifications;
    /**
     * 商品参数存储
     */
    @Column(name = "GOODS_PARAM_VALUE_STORE", length = 3000)
    private String goodsParameterValueStore;// 商品参数存储
    /**
     * 所属店铺信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOP_ID", foreignKey = @ForeignKey(name = "FK_GOODS_SHOP"))
    private Shop shop;

    /**
     * 获取商品ID
     *
     * @return {Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * 修改商品ID
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品编码
     *
     * @return {}
     */
    public String getSn() {
        return sn;
    }

    /**
     * 修改商品编码
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 修改商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取销售价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 修改销售价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取商品英文名称
     */
    public String getEngname() {
        return engname;
    }

    /**
     * 修改商品英文名称
     */
    public void setEngname(String engname) {
        this.engname = engname;
    }

    /**
     * 获取市场价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * 修改市场价
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取商品重量
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 修改商品重量
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取库存
     */
    public Integer getStore() {
        return store;
    }

    /**
     * 修改库存
     */
    public void setStore(Integer store) {
        this.store = store;
    }

    /**
     * 获取被占用库存数
     */
    public Integer getFreezeStore() {
        return freezeStore;
    }

    /**
     * 修改被占用库存数
     */
    public void setFreezeStore(Integer freezeStore) {
        this.freezeStore = freezeStore;
    }

    /**
     * 获取积分
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 修改积分
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取品牌对象
     */
    public Brand getBrand() {
        return brand;
    }

    /**
     * 修改品牌对象
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * 获取收藏集合
     */
    public List<Member> getFavoriteMembers() {
        return favoriteMembers;
    }

    /**
     * 修改收藏集合
     */
    public void setFavoriteMembers(List<Member> favoriteMembers) {
        this.favoriteMembers = favoriteMembers;
    }

    /**
     * 获取货品集合
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * 修改货品集合
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * 获取商品分类
     */
    public GoodsCategory getCategory() {
        return category;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public void setCategory(GoodsCategory category) {
        this.category = category;
    }

    public Integer getMonthSaleCount() {
        return monthSaleCount;
    }

    public void setMonthSaleCount(Integer monthSaleCount) {
        this.monthSaleCount = monthSaleCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    //@TypeConversion(key = "introduction", converter = "com.fantasy.common.bean.converter.HtmlConverter")
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Boolean getMarketable() {
        return marketable;
    }

    public void setMarketable(Boolean marketable) {
        this.marketable = marketable;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public Boolean getSpecificationEnabled() {
        return specificationEnabled;
    }

    public void setSpecificationEnabled(Boolean specificationEnabled) {
        this.specificationEnabled = specificationEnabled;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }


    @JsonIgnore
    public String getGoodsImageStore() {
        return goodsImageStore;
    }

    public void setGoodsImageStore(String goodsImageStore) {
        this.goodsImageStore = goodsImageStore;
    }

    @JsonIgnore
    public String getGoodsParameterValueStore() {
        return goodsParameterValueStore;
    }

    public void setGoodsParameterValueStore(String goodsParameterValueStore) {
        this.goodsParameterValueStore = goodsParameterValueStore;
    }

    // 获取默认货品
    @Transient
    @JsonIgnore
    public Product getDefaultProduct() {
        if (getProducts() == null || getProducts().isEmpty()) {
            return null;
        }
        if (specificationEnabled) {
            for (Product product : getProducts()) {
                if (product.getIsDefault()) {
                    return product;
                }
            }
        } else {
            return getProducts().get(0);
        }
        return null;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    @Transient
    private List<GoodsParameterValue> goodsParameterValues;
    @Transient
    private List<GoodsParameterValue> customGoodsParameterValues;

    public void setGoodsParameterValues(List<GoodsParameterValue> goodsParameterValues) {
        this.goodsParameterValues = goodsParameterValues;
    }

    /**
     * 获取商品参数
     */
    public List<GoodsParameterValue> getGoodsParameterValues() {
        if (this.goodsParameterValues == null && StringUtil.isNotBlank(this.goodsParameterValueStore)) {
            this.goodsParameterValues = JSON.deserialize(this.goodsParameterValueStore, new TypeReference<List<GoodsParameterValue>>() {
            });
        }
        return this.goodsParameterValues;
    }

    public List<GoodsParameterValue> getCustomGoodsParameterValues() {
        if (this.customGoodsParameterValues == null && StringUtil.isNotBlank(this.goodsParameterValueStore)) {
            this.customGoodsParameterValues = new ArrayList<GoodsParameterValue>();
            for (GoodsParameterValue goodsParameterValue : getGoodsParameterValues()) {
                if (ObjectUtil.find(this.getCategory().getGoodsParameters(), "id", goodsParameterValue.getId()) == null) {
                    this.customGoodsParameterValues.add(goodsParameterValue);
                }
            }
            for (GoodsParameterValue goodsParameterValue : this.customGoodsParameterValues) {
                this.goodsParameterValues.remove(goodsParameterValue);
            }
        }
        return customGoodsParameterValues;
    }

    public void setCustomGoodsParameterValues(List<GoodsParameterValue> customGoodsParameterValues) {
        this.customGoodsParameterValues = customGoodsParameterValues;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Transient
    public void setGoodsImages(GoodsImage[] goodsImages) {
        this.setGoodsImageStore(JSON.serialize(goodsImages));
    }

    /**
     * 获取商品图片集合
     */
    @Transient
    public GoodsImage[] getGoodsImages() {
        if (StringUtils.isEmpty(this.goodsImageStore)) {
            return new GoodsImage[0];
        }
        return JSON.deserialize(this.goodsImageStore, GoodsImage[].class);
    }

    @Transient
    public GoodsImage getDefaultGoodsImage() {
        GoodsImage[] goodsImages = getGoodsImages();
        if (goodsImages != null && goodsImages.length > 0) {
            return goodsImages[0];
        } else {
            return null;
        }
    }

    @Transient
    public String getDefaultGoodsImagePath() {
        GoodsImage goodsImage = getDefaultGoodsImage();
        return goodsImage != null ? goodsImage.getSourceImagePath() : SettingUtil.get("DefaultGoodsImagePath");
    }

    // 获取默认商品图片（大）
    @Transient
    public String getDefaultBigGoodsImagePath() {
        GoodsImage goodsImage = getDefaultGoodsImage();
        return goodsImage != null ? goodsImage.getBigImagePath() : SettingUtil.get("DefaultBigGoodsImagePath");
    }

    // 获取默认商品图片（小）
    @Transient
    public String getDefaultSmallGoodsImagePath() {
        GoodsImage goodsImage = getDefaultGoodsImage();
        return goodsImage != null ? goodsImage.getSmallImagePath() : SettingUtil.get("DefaultSmallGoodsImagePath");
    }

    // 获取默认商品图片（缩略图）
    @Transient
    public String getDefaultThumbnailGoodsImagePath() {
        GoodsImage goodsImage = getDefaultGoodsImage();
        return goodsImage != null ? goodsImage.getSmallImagePath() : SettingUtil.get("DefaultThumbnailGoodsImagePath");
    }

}
