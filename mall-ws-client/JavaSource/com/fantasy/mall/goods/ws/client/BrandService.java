package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.mall.goods.ws.IBrandService;

import com.fantasy.mall.goods.ws.dto.BrandDTO;

/**
 * Created by 县宜 on 2014/6/30.
 */
public class BrandService extends WebServiceClient implements IBrandService {

    public BrandService() {// 设置对应的接口名称
        super("BrandService");
    }


    @Override
    public BrandDTO[] brands() {
        return super.invokeOption("brands", new Object[]{}, BrandDTO[].class);
    }


    @Override
    public BrandDTO[] goodsCategory(String sign) {
        return super.invokeOption("goodsCategory",new Object[]{sign},BrandDTO[].class);
    }

}
