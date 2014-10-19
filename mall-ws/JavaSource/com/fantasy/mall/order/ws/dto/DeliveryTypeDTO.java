package com.fantasy.mall.order.ws.dto;


import java.math.BigDecimal;

public class DeliveryTypeDTO {


    private Long id;


    private String name;// 配送方式名称

    private String  method;// 配送类型

    private Integer firstWeight;// 首重量(单位: 克)

    private Integer continueWeight;// 续重量(单位: 克)

    private BigDecimal firstWeightPrice;// 首重价格

    private BigDecimal continueWeightPrice;// 续重价格

    private String description;// 介绍


    //private DeliveryCorp defaultDeliveryCorp; // 默认物流公司


   // private List<Order> orders;// 订单

   // private List<Shipping> shippings;// 发货

    //private List<Reship> reships;// 退货


    public BigDecimal getContinueWeightPrice() {
        return continueWeightPrice;
    }

    public void setContinueWeightPrice(BigDecimal continueWeightPrice) {
        this.continueWeightPrice = continueWeightPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFirstWeightPrice() {
        return firstWeightPrice;
    }

    public void setFirstWeightPrice(BigDecimal firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Integer getContinueWeight() {
        return continueWeight;
    }

    public void setContinueWeight(Integer continueWeight) {
        this.continueWeight = continueWeight;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

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
}
