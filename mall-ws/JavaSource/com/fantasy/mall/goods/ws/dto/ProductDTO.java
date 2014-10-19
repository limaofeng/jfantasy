package com.fantasy.mall.goods.ws.dto;

import com.fantasy.file.ws.dto.FileDTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供应商商品
 *
 * @author 李县宜
 * @version 1.0
 * @since 2014-6-21 下午2:46:20
 */
public class ProductDTO implements Serializable {

    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品编号
     */
    private String sn;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品英文名称
     */
    private String engname;

    /**
     * 销售价
     */
    private BigDecimal price;

    /**
     * 成本价
     */
    private BigDecimal cost;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 商品重量(单位: 克)
     */
    private Integer weight;
    /**
     * 库存
     */
    private Integer store;
    /**
     * 被占用库存数
     */
    private Integer freezeStore;
    /**
     * 月销量(近30天的)
     */
    private Integer monthSaleCount;
    /**
     * 销量(总)
     */
    private Integer saleCount;
    /**
     * 积分
     */
    private Integer score;
    /**
     * 是否上架
     */
    private Boolean marketable;
    /**
     * 是否为精品商品
     */
    private Boolean isBest;
    /**
     * 是否为新品商品
     */
    private Boolean isNew;
    /**
     * 是否为热销商品
     */
    private Boolean isHot;
    /**
     * 产品介绍
     */
    private String introduction;
    /**
     * 页面关键词
     */
    private String metaKeywords;
    /**
     * 页面描述
     */
    private String metaDescription;
    /**
     * 是否启用商品规格
     */
    private Boolean specificationEnabled;

    /**
     * 品牌
     */
    private BrandDTO brand;

    /**
     * 销售特色语/产品描述
     */

    private String saleslanguage;


    /**
     * 商品所属分类
     */
    private String type;
    /**
     * 视频存储地址
     */
    private FileDTO video;

    /**
     * 封面图存储地址
     */
    private GoodsImageDTO coverImage;

    /**
     * 规格参数
     */
    private GoodsParameterValueDTO[] goodsParameterValues;

    /**
     * 图片欣赏
     */
    private GoodsImageDTO[] goodsImages;

    /**
     * 平面图存储地址
     */
    private GoodsImageDTO[] planeGoodsImages;

    /**
     * 全景图存储地址
     */
    private GoodsImageDTO[] panoramaGoodsImages;


    /**
     * 是否推荐
     */

    private Boolean recommended;

    /**
     * 人数
     */

    private Integer number;


    /**
     * 地址
     */

    private String address;


    /**
     * 产品分类
     */
    private String gdtype;


    /**
     * 产地
     */
    private String countries;


    /**
     * 艇长
     */

    private Integer yachtlength;


    /**
     * 套餐
     */

    private SpecialDTO[] specials;


    /**
     * 时间
     */

    private DisabledTimeDTO[] disabledTimes;


    /**
     * 供应商ID
     */
    private Long supplierID;


    /**
     * 供应商名称
     */
    private String supplierName;


    /**
     * 系列名称
     */
    private String series;


    /**
     * 型号名称
     */
    private String yachtModel;


    /**
     * 供应商商品编码ID
     */
    private String supplierCode;





    /**
     * 船舱
     */
    private  Integer cabin;




    /**
     * 港湾描述
     */
    private  String  harbourdescribe;

    /**
     * 速度
     */
    private   String   speed;


    public String getHarbourdescribe() {
        return harbourdescribe;
    }

    public void setHarbourdescribe(String harbourdescribe) {
        this.harbourdescribe = harbourdescribe;
    }

    public Integer getCabin() {
        return cabin;
    }

    public void setCabin(Integer cabin) {
        this.cabin = cabin;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSaleslanguage() {
        return saleslanguage;
    }

    public void setSaleslanguage(String saleslanguage) {
        this.saleslanguage = saleslanguage;
    }


    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getGdtype() {
        return gdtype;
    }

    public void setGdtype(String gdtype) {
        this.gdtype = gdtype;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public Integer getYachtlength() {
        return yachtlength;
    }

    public void setYachtlength(Integer yachtlength) {
        this.yachtlength = yachtlength;
    }

    public SpecialDTO[] getSpecials() {
        return specials;
    }

    public void setSpecials(SpecialDTO[] specials) {
        this.specials = specials;
    }

    public DisabledTimeDTO[] getDisabledTimes() {
        return disabledTimes;
    }

    public void setDisabledTimes(DisabledTimeDTO[] disabledTimes) {
        this.disabledTimes = disabledTimes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getYachtModel() {
        return yachtModel;
    }

    public void setYachtModel(String yachtModel) {
        this.yachtModel = yachtModel;
    }

    /**
     * 获取商品ID
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * 修改商品ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品编码
     *
     * @return
     */
    public String getSn() {
        return sn;
    }

    /**
     * 修改商品编码
     *
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取商品名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 修改商品名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取销售价
     *
     * @return
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 修改销售价
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成本价
     *
     * @return
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 修改成本价
     *
     * @param cost
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取商品英文名称
     *
     * @return
     */
    public String getEngname() {
        return engname;
    }

    /**
     * 修改商品英文名称
     *
     * @param engname
     */
    public void setEngname(String engname) {
        this.engname = engname;
    }

    /**
     * 获取市场价
     *
     * @return
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * 修改市场价
     *
     * @param marketPrice
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取商品重量
     *
     * @return
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 修改商品重量
     *
     * @param weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取库存
     *
     * @return
     */
    public Integer getStore() {
        return store;
    }

    /**
     * 修改库存
     *
     * @param store
     */
    public void setStore(Integer store) {
        this.store = store;
    }

    /**
     * 获取被占用库存数
     *
     * @return
     */
    public Integer getFreezeStore() {
        return freezeStore;
    }

    /**
     * 修改被占用库存数
     *
     * @param freezeStore
     */
    public void setFreezeStore(Integer freezeStore) {
        this.freezeStore = freezeStore;
    }

    /**
     * 获取积分
     *
     * @return
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 修改积分
     *
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
    }


    /**
     * 获取品牌对象
     *
     * @return
     */
    public BrandDTO getBrand() {
        return brand;
    }

    /**
     * 修改品牌对象
     *
     * @param brand
     */
    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }


    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
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

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Boolean getMarketable() {
        return marketable;
    }

    public void setMarketable(Boolean marketable) {
        this.marketable = marketable;
    }

    public Boolean getIsBest() {
        return isBest;
    }

    public void setIsBest(Boolean isBest) {
        this.isBest = isBest;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
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

    public FileDTO getVideo() {
        return video;
    }

    public void setVideo(FileDTO video) {
        this.video = video;
    }

    public GoodsImageDTO[] getPlaneGoodsImages() {
        return planeGoodsImages;
    }

    public void setPlaneGoodsImages(GoodsImageDTO[] planeGoodsImages) {
        this.planeGoodsImages = planeGoodsImages;
    }

    public GoodsImageDTO[] getPanoramaGoodsImages() {
        return panoramaGoodsImages;
    }

    public void setPanoramaGoodsImages(GoodsImageDTO[] panoramaGoodsImages) {
        this.panoramaGoodsImages = panoramaGoodsImages;
    }

    public GoodsImageDTO getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(GoodsImageDTO coverImage) {
        this.coverImage = coverImage;
    }

    public GoodsParameterValueDTO[] getGoodsParameterValues() {
        return goodsParameterValues;
    }

    public void setGoodsParameterValues(
            GoodsParameterValueDTO[] goodsParameterValues) {
        this.goodsParameterValues = goodsParameterValues;
    }

    public GoodsImageDTO[] getGoodsImages() {
        return goodsImages;
    }

    public void setGoodsImages(GoodsImageDTO[] goodsImages) {
        this.goodsImages = goodsImages;
    }
}
