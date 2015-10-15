package com.fantasy.mall.goods.bean;

import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.attr.storage.BaseDynaBean;
import com.fantasy.framework.dao.mybatis.keygen.util.SequenceInfo;
import com.fantasy.framework.spring.SpELUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.cart.bean.CartItem;
import com.fantasy.mall.goods.bean.converter.GoodsImageConverter;
import com.fantasy.mall.order.bean.OrderItem;
import com.fantasy.mall.stock.bean.Stock;
import com.fantasy.mall.stock.bean.WarningSettings;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 货品表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午5:04:43
 */
@Entity
@Table(name = "MALL_PRODUCT")
@Persister(impl = DynaBeanEntityPersister.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "specificationValueStore", "goodsNotifys", "goodsImage", "goodsImageStore", "cartItems", "orderItems", "warningSettings"})
public class Product extends BaseDynaBean {

    private static final long serialVersionUID = -4663151563624172169L;

    public void initialize(Goods goods) {
        if (this.getId() == null) {
            // 默认值字段
            this.setStore(0);
            this.setFreezeStore(0);
            // 关联商品
            this.setGoods(goods);
        }
        // goods相同的属性
        if (!goods.getSpecificationEnabled()) {
            this.setSn(goods.getSn());
        } else {
            this.setSn(goods.getSn() + "-" + SequenceInfo.nextValue("PRODUCT_SN" + goods.getSn()));
        }
        if (StringUtil.isBlank(this.getName())) {
            this.setName(goods.getName());
        }

        if (StringUtil.isBlank(this.getPrice())) {
            this.setPrice(goods.getPrice());
        }

        if (StringUtil.isBlank(this.getCost())) {
            this.setCost(goods.getCost());
        }

        if (StringUtil.isBlank(this.getMarketPrice())) {
            this.setMarketPrice(goods.getMarketPrice());
        }

        if (StringUtil.isBlank(this.getMarketable())) {
            this.setMarketable(goods.getMarketable());
        }

        if (StringUtil.isBlank(this.getWeight())) {
            this.setWeight(goods.getWeight());
        }

        if (this.getGoodsImage() == null && goods.getGoodsImages() != null && goods.getGoodsImages().length != 0) {// product只保存一张图片
            this.setGoodsImage(goods.getGoodsImages()[0]);
        }
    }

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @Column(name = "SN", nullable = false, unique = true)
    private String sn;// 货品编号
    @Column(name = "NAME", nullable = false)
    private String name;// 名称
    @Column(name = "PRICE", nullable = false, precision = 15, scale = 5)
    private BigDecimal price;// 销售价
    @Column(name = "COST", precision = 15, scale = 5)
    private BigDecimal cost;// 成本价
    @Column(name = "MARKET_PRICE", nullable = false, precision = 15, scale = 5)
    private BigDecimal marketPrice;// 市场价
    @Column(name = "WEIGHT", nullable = false)
    private Integer weight;// 商品重量(单位: 克)
    @Column(name = "STORE", nullable = false)
    private Integer store;// 库存
    @Column(name = "FREEZE_STORE", nullable = false)
    private Integer freezeStore;// 被占用库存数
    @Column(name = "STORE_PLACE", nullable = true)
    private String storePlace;// 货位
    @Column(name = "MARKETABLE", nullable = false)
    private Boolean marketable;// 是否上架
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault;// 是否默认
    @Column(name = "SPECIFICATION_VALUE_STORE", length = 3000)
    private String specificationValueStore;// 商品规格值存储
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCT_GOODS"))
    private Goods goods;// 商品
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<GoodsNotify> goodsNotifys;// 到货通知
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<CartItem> cartItems;// 购物车项
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<OrderItem> orderItems;// 订单项
    @Column(name = "GOODS_IMAGE_STORE", length = 3000)
    @Convert(converter = GoodsImageConverter.class)
    private GoodsImage goodsImage;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Stock> stocks;// 库存变量
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "WARNINGSETTINGS_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_WARNINGSETTINGS"))
    private WarningSettings warningSettings;

    @Transient
    public Integer getSurplusStore() {// 可用库存
        int availableStock = store - freezeStore;
        return availableStock < 0 ? 0 : availableStock;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = ObjectUtil.defaultValue(weight, 0);
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getFreezeStore() {
        return freezeStore;
    }

    public void setFreezeStore(Integer freezeStore) {
        this.freezeStore = ObjectUtil.defaultValue(freezeStore, 0);
    }

    public String getStorePlace() {
        return storePlace;
    }

    public void setStorePlace(String storePlace) {
        this.storePlace = storePlace;
    }

    public Boolean getMarketable() {
        return marketable;
    }

    public void setMarketable(Boolean marketable) {
        this.marketable = marketable;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getSpecificationValueStore() {
        return specificationValueStore;
    }

    public void setSpecificationValueStore(String specificationValueStore) {
        this.specificationValueStore = specificationValueStore;
    }

    public List<GoodsNotify> getGoodsNotifys() {
        return goodsNotifys;
    }

    public void setGoodsNotifys(List<GoodsNotify> goodsNotifys) {
        this.goodsNotifys = goodsNotifys;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public WarningSettings getWarningSettings() {
        return warningSettings;
    }

    public void setWarningSettings(WarningSettings warningSettings) {
        this.warningSettings = warningSettings;
    }

    @Transient
    public boolean isWarning() {
        if (this.getWarningSettings() == null) {
            return false;
        }
        return SpELUtil.getExpression(this.getWarningSettings().getExpression()).getValue(SpELUtil.createEvaluationContext(this), Boolean.class);
    }

    /**
     * 获取商品图片
     *
     * @return GoodsImage
     */
    public GoodsImage getGoodsImage() {
        return this.goodsImage;
    }

    public void setGoodsImage(GoodsImage goodsImage) {
        this.goodsImage = goodsImage;
    }

    /**
     * 获取默认商品图片（大）
     *
     * @return String
     */
    @Transient
    public String getDefaultBigGoodsImagePath() {
        GoodsImage goodsImage = getGoodsImage();
        if (goodsImage != null) {
            return goodsImage.getBigImagePath();
        } else {
            return "";
        }
    }

    /**
     * 获取默认商品图片（小）
     *
     * @return String
     */
    @Transient
    public String getDefaultSmallGoodsImagePath() {
        GoodsImage goodsImage = getGoodsImage();
        if (goodsImage != null) {
            return goodsImage.getSmallImagePath();
        } else {
            return "";
        }
    }

    /**
     * 获取默认商品图片（缩略图）
     *
     * @return String
     */
    @Transient
    public String getDefaultThumbnailGoodsImagePath() {
        GoodsImage goodsImage = getGoodsImage();
        if (goodsImage != null) {
            return goodsImage.getThumbnailImagePath();
        } else {
            return "";
        }
    }
}