package org.test;

import java.math.BigDecimal;

/**
 * Created by lmf on 14-9-17.
 */
public class GoodsDTO {

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
     * 港湾描述
     */
    private String metaDescription;
    /**
     * 是否启用商品规格
     */
    private Boolean specificationEnabled;


    /**
     * 销售特色语/产品描述
     */

    private String saleslanguage;



    /**
     * 港湾描述
     */

    private  String  harbourdescribe;


    public String getHarbourdescribe() {
        return harbourdescribe;
    }

    public void setHarbourdescribe(String harbourdescribe) {
        this.harbourdescribe = harbourdescribe;
    }

    public String getSaleslanguage() {
        return saleslanguage;
    }

    public void setSaleslanguage(String saleslanguage) {
        this.saleslanguage = saleslanguage;
    }



    /**
     *
     * 是否推荐
     */

    private Boolean recommended;

    /**
     *
     * 人数
     */

    private Integer number;




    /**
     * 船舱
     */

    private  Integer cabin;


    /**
     * 速度
     */

    private   String   speed;



    /**
     *
     * 地址
     */

    private String address;



    /**
     * 产品分类
     */
    private  String gdtype;


    /**
     * 原产国
     */
    private String countries;


    /**
     * 艇长
     */

    private Double yachtlength;



    /**
     * 供应商ID
     *
     */


    private  Long  supplierId;




    /**
     * 供应商名称
     *
     */

    private  String   supplierName;



    /**
     * 供应商商品编码
     *
     */
    private String supplierCode;


    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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

    public Double getYachtlength() {
        return yachtlength;
    }

    public void setYachtlength(Double yachtlength) {
        this.yachtlength = yachtlength;
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




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取商品ID
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * 修改商品ID
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品编码
     * @return
     */
    public String getSn() {
        return sn;
    }

    /**
     * 修改商品编码
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取商品名称
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 修改商品名称
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取销售价
     * @return
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 修改销售价
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成本价
     * @return
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 修改成本价
     * @param cost
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取商品英文名称
     * @return
     */
    public String getEngname() {
        return engname;
    }

    /**
     * 修改商品英文名称
     * @param engname
     */
    public void setEngname(String engname) {
        this.engname = engname;
    }

    /**
     * 获取市场价
     * @return
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * 修改市场价
     * @param marketPrice
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取商品重量
     * @return
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 修改商品重量
     * @param weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取库存
     * @return
     */
    public Integer getStore() {
        return store;
    }

    /**
     * 修改库存
     * @param store
     */
    public void setStore(Integer store) {
        this.store = store;
    }

    /**
     * 获取被占用库存数
     * @return
     */
    public Integer getFreezeStore() {
        return freezeStore;
    }

    /**
     * 修改被占用库存数
     * @param freezeStore
     */
    public void setFreezeStore(Integer freezeStore) {
        this.freezeStore = freezeStore;
    }

    /**
     * 获取积分
     * @return
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 修改积分
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
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


}
