package com.fantasy.mall.goods.bean;

import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * 商品分类
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-21 下午5:46:48
 */
@Entity
@Table(name = "MALL_GOODS_CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "metaKeywords", "metaDescription", "goodsParameterStore", "brandCustomSort", "goods"})
public class GoodsCategory extends BaseBusEntity {

    private static final long serialVersionUID = -5132652107151648662L;

    public static final String PATH_SEPARATOR = ",";// 树路径分隔符

    /**
     * 分类ID
     */
    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 分类名称
     */
    @Column(name = "NAME", nullable = false)
    private String name;
    /**
     * 分类标识
     */
    @Column(name = "SIGN", nullable = false, unique = true)
    private String sign;
    /**
     * 页面关键词
     */
    @Column(name = "META_KEYWORDS", length = 3000)
    private String metaKeywords;// 页面关键词
    /**
     * 页面描述
     */
    @Column(name = "META_DESCRIPTION", length = 3000)
    private String metaDescription;// 页面描述
    /**
     * 排序
     */
    @Column(name = "SORT")
    private Integer sort;// 排序
    /**
     * 树路径
     */
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;// 树路径
    /**
     * 层级
     */
    @Column(name = "LAYER", nullable = false)
    private Integer layer;
    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "P_ID", foreignKey = @ForeignKey(name = "FK_GOODS_CATEGORY_PARENT"))
    @JsonManagedReference
    private GoodsCategory parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy("sort ASC")
    @JsonBackReference
    private List<GoodsCategory> children;
    /**
     * 属性版本表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_VERSION_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_CATEGORY_VERSION"))
    private AttributeVersion goodsVersion;
    /**
     * 商品
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Goods> goods;
    /**
     * 商品品牌
     */
    @ManyToMany(mappedBy = "goodsCategories", fetch = FetchType.LAZY)
    @OrderBy("name asc")
    //@JoinTable(name = "MALL_GOODS_CATEGORY_BRAND", joinColumns = @JoinColumn(name = "GOODS_CATEGORY_ID"), inverseJoinColumns = @JoinColumn(name = "BRAND_ID"))
    private List<Brand> brands;
    /**
     * 品牌在分类中的排序规则
     */
    @Column(name = "BRAND_CUSTOM_SORT", length = 100)
    private String brandCustomSort;
    /**
     * 商品参数存储
     */
    @Column(name = "GOODS_PARAMETER_STORE", length = 3000)
    private String goodsParameterStore;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    @JsonProperty("parentId")
    @JsonSerialize(using = CategoryParentSerialize.class)
    public GoodsCategory getParent() {
        return parent;
    }

    public void setParent(GoodsCategory parent) {
        this.parent = parent;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public List<GoodsCategory> getChildren() {
        return children;
    }

    public void setChildren(List<GoodsCategory> children) {
        this.children = children;
    }

    public List<Brand> getBrands() {
        if (brands == null || StringUtil.isBlank(brandCustomSort)) {
            return brands;
        }
        return ObjectUtil.sort(brands, StringUtil.tokenizeToStringArray(this.brandCustomSort), "id");
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public String getBrandCustomSort() {
        return brandCustomSort;
    }

    public void setBrandCustomSort(String brandCustomSort) {
        this.brandCustomSort = brandCustomSort;
    }

    @JsonIgnore
    public String[] getBrandCustomSortIds() {
        if (StringUtil.isBlank(this.brandCustomSort)) {
            return new String[0];
        }
        return StringUtil.tokenizeToStringArray(this.brandCustomSort);
    }

    public Long[] getBrandIds() {
        if (this.getBrands() == null)
            return new Long[0];
        return ObjectUtil.toFieldArray(this.getBrands(), "id", Long.class);
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGoodsParameterStore() {
        return goodsParameterStore;
    }

    public void setGoodsParameterStore(String goodsParameterStore) {
        this.goodsParameterStore = goodsParameterStore;
    }

    @Transient
    private List<GoodsParameter> goodsParameters;

    public void setGoodsParameters(List<GoodsParameter> goodsParameters) {
        this.goodsParameters = goodsParameters;
    }

    @JsonIgnore
    public List<GoodsParameter> getGoodsParameters() {
        if (this.goodsParameters == null && StringUtil.isNotBlank(this.goodsParameterStore)) {
            this.goodsParameters = JSON.deserialize(this.goodsParameterStore, new TypeReference<List<GoodsParameter>>() {
            });
        }
        return this.goodsParameters;
    }

    public static class CategoryParentSerialize extends JsonSerializer<GoodsCategory> {
        @Override
        public void serialize(GoodsCategory category, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(category.getId() != null ? category.getId().toString() : "");
        }
    }

    public AttributeVersion getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(AttributeVersion goodsVersion) {
        this.goodsVersion = goodsVersion;
    }

}