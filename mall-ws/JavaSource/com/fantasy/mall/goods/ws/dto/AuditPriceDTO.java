package com.fantasy.mall.goods.ws.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 申请调价的DTO
 */
public class AuditPriceDTO {

    //商品编号
    private String sn;

    //普通价格
    private BigDecimal price;

    //套餐
    private SpecialDTO[] specialDTOs;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SpecialDTO[] getSpecialDTOs() {
        return specialDTOs;
    }

    public void setSpecialDTOs(SpecialDTO[] specialDTOs) {
        this.specialDTOs = specialDTOs;
    }
}
