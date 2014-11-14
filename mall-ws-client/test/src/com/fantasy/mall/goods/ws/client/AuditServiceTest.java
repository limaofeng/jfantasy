package com.fantasy.mall.goods.ws.client;

import com.fantasy.mall.goods.ws.dto.AuditPriceDTO;
import com.fantasy.mall.goods.ws.dto.SpecialDTO;
import com.fatnasy.ws.Constants;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 商品调价
 */
public class AuditServiceTest {

   private AuditService auditService;

    @Before
    public void init() throws Exception {
        auditService = new AuditService();
        auditService.setEndPointReference(Constants.END_POINT_REFERENCE);
        auditService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        auditService.afterPropertiesSet();
    }


    /*
     *申请调价
     *
     */
    @Test
    public void priceVerify(){
        AuditPriceDTO auditPriceDTO = new AuditPriceDTO();
        //商品编号
        auditPriceDTO.setSn("SN_2014071400103");
        //商品普通价格
        auditPriceDTO.setPrice(new BigDecimal("200.20"));
        //套餐价格
        SpecialDTO specialDTO1 = new SpecialDTO();
        specialDTO1.setName("普通套餐1");
        specialDTO1.setHour("121");
        specialDTO1.setPrice(new BigDecimal("1000.00"));
        //套餐价格
        SpecialDTO specialDTO2 = new SpecialDTO();
        specialDTO2.setName("普通套餐2");
        specialDTO2.setHour("122");
        specialDTO2.setPrice(new BigDecimal("2000.00"));
        //套餐价格
        SpecialDTO specialDTO3 = new SpecialDTO();
        specialDTO3.setName("普通套餐3");
        specialDTO3.setHour("123");
        specialDTO3.setPrice(new BigDecimal("3000.00"));
        //套餐价格
        SpecialDTO specialDTO4 = new SpecialDTO();
        specialDTO4.setName("普通套餐4");
        specialDTO4.setHour("124"); //套餐价格
        specialDTO4.setPrice(new BigDecimal("4000.00"));
        SpecialDTO specialDTO5 = new SpecialDTO();
        specialDTO5.setName("普通套餐5");
        specialDTO5.setHour("125");
        specialDTO5.setPrice(new BigDecimal("5000.00"));
        //申请调价
        auditPriceDTO.setSpecialDTOs(new SpecialDTO[]{specialDTO1,specialDTO2,specialDTO3,specialDTO4,specialDTO5});
        auditService.priceVerify(auditPriceDTO);
    }


    /**
     * 下线申请
     */

    //@Test
    public void downVerify(){
        String sn="SN_2014071000102";
        String message="我想下就下，事多";
        auditService.downVerify(sn,message);
    }



 }





