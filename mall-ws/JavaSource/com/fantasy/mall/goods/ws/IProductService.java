package com.fantasy.mall.goods.ws;


import com.fantasy.mall.goods.ws.dto.ProductDTO;


public interface IProductService {

    /**
     * 保存供应商提交商品
     *
     * @param product 产品对象()
     */

    public void submitProduct(ProductDTO product);


}
