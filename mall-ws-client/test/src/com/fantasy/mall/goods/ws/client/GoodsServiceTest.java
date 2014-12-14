package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.goods.ws.dto.*;
import com.fatnasy.ws.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class GoodsServiceTest {

    private GoodsService goodsService;

    @Before
    public void init() throws Exception {
        goodsService = new GoodsService();
        goodsService.setEndPointReference(Constants.END_POINT_REFERENCE);
        goodsService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        goodsService.afterPropertiesSet();
    }

    // @Test
    public void testFindPager() throws Exception {
        PagerDTO pager = new PagerDTO();// 设置每页显示的数据条数
        // 设置过滤条件
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.sign", "rental"));
        // 调用接口查询
        GoodsPagerResult pagerResult = goodsService.findPager(pager, filters.toArray(new PropertyFilterDTO[filters.size()]));
        // PagerDTO pagination= goodsService.findPager(pager, filters.toArray(new PropertyFilterDTO[1]));
        // 打印结果
        for (GoodsDTO goodsDTO : pagerResult.getPageItems()) {

        }

    }


    //@Test
    public void testcategories() {
        if (goodsService.categories() != null) {
            for (GoodsCategoryDTO goodsCategory : goodsService.categories()) {
                System.out.println(goodsCategory);
            }
        }
    }

    //@Test
    public void testGetGoodsById() {
        Long id = Long.valueOf("82");
        GoodsDTO goodsDTO = goodsService.getGoodsById(id);

    }

    @Test
    public void testGetGoodsCategoryBySign() {
        GoodsCategoryDTO goodsCategoryDTO = goodsService.getGoodsCategoryBySign("yachtservice");
        for (int i = 0; i < goodsCategoryDTO.getChildren().length; i++) {

        }

    }

    public void testfind() {
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.sign", "berth"));
        GoodsDTO[] atinfo = goodsService.find(filters.toArray(new PropertyFilterDTO[filters.size()]), null, null, 25);
        for (int i = 0; i < atinfo.length; i++) {

        }
    }
}
