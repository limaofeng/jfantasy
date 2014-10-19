package com.fantasy.mall.goods.ws.client;

import com.fantasy.mall.goods.ws.dto.ProductDTO;
import com.fatnasy.ws.Constants;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;


public class ProductServiceTest {

    private ProductService productService;
    @Before
    public void init() throws Exception {
        productService = new ProductService();
        productService.setEndPointReference(Constants.END_POINT_REFERENCE);
        productService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        productService.afterPropertiesSet();
    }


    @Test
    public void testsupplyGoods(){
        ProductDTO product=new ProductDTO();
        product.setName("供应商产品77520");
        product.setEngname("asdasd");
        product.setType("rental");

        BigDecimal bigDecimal = new BigDecimal(236723);
        product.setMarketPrice(bigDecimal);
        product.setPrice(bigDecimal);
        productService.submitProduct(product);

    }

}
