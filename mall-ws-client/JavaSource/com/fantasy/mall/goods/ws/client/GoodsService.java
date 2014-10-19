package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.goods.ws.IGoodsService;
import com.fantasy.mall.goods.ws.dto.BrandDTO;
import com.fantasy.mall.goods.ws.dto.GoodsCategoryDTO;
import com.fantasy.mall.goods.ws.dto.GoodsDTO;
import com.fantasy.mall.goods.ws.dto.ProductDTO;

import java.util.List;


/**
 * 商品接口测试类
 * 
 * @功能描述 <br/>
 *       http://localhost:8080/bth/services/GoodsService?wsdl 接口描述地址
 * 
 * @author 李茂峰
 * @since 2014-4-3 下午12:50:27
 * @version 1.0
 */
public class GoodsService extends WebServiceClient implements IGoodsService {

    public GoodsService() {// 设置对应的接口名称
        super("GoodsService");
    }



    @Override
    public PagerDTO findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
        return (PagerDTO) super.invokeOption("findPager", new Object[] { pager, filters }, new Class[] { PagerDTO.class, List.class, GoodsDTO.class });
    }

    @Override
    public GoodsDTO getGoodsById(Long id) {
        return (GoodsDTO) super.invokeOption("getGoodsById", new Object[] { id }, GoodsDTO.class);
    }

    @Override
    public GoodsCategoryDTO getGoodsCategoryBySign(String sign) {
        return super.invokeOption("getGoodsCategoryBySign", new Object[] { sign }, GoodsCategoryDTO.class);
    }

    @Override
    public GoodsCategoryDTO[] categories() {
        return super.invokeOption("categories", new Object[] {}, GoodsCategoryDTO[].class);
    }

    @Override
    public GoodsDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
        return super.invokeOption("find", new Object[] { filters, orderBy, order,size }, GoodsDTO[].class);
    }
}
