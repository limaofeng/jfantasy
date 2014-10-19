package com.fantasy.mall.goods.ws.client;

import com.fantasy.mall.goods.ws.dto.BrandDTO;
import com.fantasy.mall.goods.ws.dto.BrandSeriesDTO;
import com.fantasy.mall.goods.ws.dto.SeriesModelDTO;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by 县宜 on 2014/6/23.
 */
public class BrandServiceTest {

    /**
     * ----------------------------------* 往下为 JUNIT 测试 --------------------------------
     *
     * --
     */



   private BrandService brandService;


    @Before
    public void init() throws Exception {
        brandService = new BrandService();
        brandService.setEndPointReference("http://127.0.0.1:8080/services");
        brandService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        brandService.setAxis2xml("classpath:axis2.xml");
        brandService.afterPropertiesSet();
    }


    @Test
    public void testproductBrands() {
                      BrandDTO[] brand = brandService.brands();
                for (BrandDTO brandDTO : brandService.brands()) {
                    System.out.println("平牌名=====" + brandDTO.getName());
                    if(brandDTO.getBrandseries()==null){
                        continue;
                }
                for(BrandSeriesDTO xilie: brandDTO.getBrandseries()){
                   System.out.println("该品牌的系列为=====" + xilie.getName());
                }
              for (SeriesModelDTO xinhao : brandDTO.getSeriesmodel()) {
                      System.out.println("该品牌的型号为=====" + xinhao.getName());
                  System.out.println("该品牌的系列为=====" + xinhao.getSeriesId());
                }
          }
     }

    @Test
    public void goodsCategory(){
        BrandDTO[] brandDTOs = brandService.goodsCategory("yacht");
        for(BrandDTO brandDTO:brandDTOs){
            System.out.println("yacht----"+brandDTO.getName());
        }

    }

 }





