package com.fantasy.mall.goods.ws;

import com.fantasy.mall.goods.ws.dto.BrandDTO;

public  interface IBrandService {


    /**
     * 全部商品品牌信息
     *
     * @功能描述
     * @param 无
     * @return Brand品牌数组
     */
    public BrandDTO[] brands();

    /**
     * 根据商品分类 ，来获取品牌
     * @param sign
     * @return
     */
    public BrandDTO[] goodsCategory(String sign);


}
