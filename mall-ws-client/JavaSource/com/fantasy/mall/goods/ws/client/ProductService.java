package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.mall.goods.ws.IProductService;
import com.fantasy.mall.goods.ws.dto.ProductDTO;


/**
 * 供应商商品接口测试类
 *
 * @author 李县宜
 * @version 1.0
 * @since 2014-4-3 下午12:50:27
 */
public class ProductService extends WebServiceClient implements IProductService {


    public ProductService() {// 设置对应的接口名称
        super("ProductService");
    }


    @Override
    public void submitProduct(ProductDTO product) {
        super.invokeOption("submitProduct", new Object[]{product}, new Class[]{});
    }


}
