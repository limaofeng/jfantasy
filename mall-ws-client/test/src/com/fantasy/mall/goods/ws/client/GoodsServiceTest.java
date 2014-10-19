package com.fantasy.mall.goods.ws.client;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.goods.ws.dto.BrandDTO;
import com.fantasy.mall.goods.ws.dto.BrandSeriesDTO;
import com.fantasy.mall.goods.ws.dto.GoodsCategoryDTO;
import com.fantasy.mall.goods.ws.dto.GoodsDTO;
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

        //	pager.setPageSize(4);
        //pager.setCurrentPage(1);
        //pager.setOrderBy("releaseDate");





        // 设置过滤条件
        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.sign","rental"));
        //filters.add(new PropertyFilterDTO("BETWEENI_yachtlength","0-100"));
        //filters.add(new PropertyFilterDTO("EQS_gdtype","jidongting"));
        //filters.add(new PropertyFilterDTO("LIKES_brand.pinyin_OR_brand.engname","S"));
        // filters.add(new PropertyFilterDTO("LIKES_brand.pinyin","S% "));
        // 调用接口查询
        PagerDTO pagination = goodsService.findPager(pager, filters.toArray(new PropertyFilterDTO[filters.size()]));
        // PagerDTO pagination= goodsService.findPager(pager, filters.toArray(new PropertyFilterDTO[1]));
        System.out.println("cccc"+pagination);
        // 打印结果
        System.out.println(pagination.getPageItems());
        for (GoodsDTO goodsDTO : pagination.getPageItems(new GoodsDTO[0])) {
            System.out.println("aaaaaaaaaa"+goodsDTO.getDisabledTimes());


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
        System.out.println("aa");
        Long id = Long.valueOf("82");
        GoodsDTO pagination = goodsService.getGoodsById(id);
        //System.out.println("aaaaaaaaaarr"+pagination.getCoverImage().getAbsolutePath());
        //System.out.println("aaaaaaaaaarr"+pagination.getHarImage().getAbsolutePath());
        // for (int i = 0; i < pagination.getDisabledTimes().length; i++) {
        //	System.out.println("iiiiiiiiii"+ pagination.getDisabledTimes()[i].getTime());

        //}
        System.out.println("aa"+pagination.getSn());
        //  System.out.println("aa"+pagination.getDisabledTimes().length);
        //	System.out.println("ccccccccccrryyy"+pagination.getAddress());

    }

    @Test
    public void testGetGoodsCategoryBySign() {
        //GoodsCategoryDTO goodsCategoryDTO = goodsService.getGoodsCategoryBySign("found");
        GoodsCategoryDTO goodsCategoryDTO = goodsService.getGoodsCategoryBySign("yachtservice");
        for (int i = 0; i < goodsCategoryDTO.getChildren().length; i ++) {
            System.out.println("zzzzzzzzzzzzzzzzzz"+goodsCategoryDTO.getChildren()[i].getName());

        }

    }
   // @Test
    public void testfind(){

        List<PropertyFilterDTO> filters = new ArrayList<PropertyFilterDTO>();
        filters.add(new PropertyFilterDTO("EQS_category.sign", "berth"));
        GoodsDTO[] atinfo = goodsService.find(filters.toArray(new PropertyFilterDTO[filters.size()]), null, null, 25);
        System.out.println("result of atinfo: " + atinfo);
        for (int i = 0; i < atinfo.length; i ++) {
            System.out.println("iiiiiiiiii"+atinfo[i].getName());

        }
    }
}
