package org.jfantasy.mall.goods.rest;

import org.jfantasy.mall.goods.bean.GoodsCategory;
import org.jfantasy.mall.goods.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "mall-categorys", description = "商品分类")
@RestController
@RequestMapping("/mall/categorys")
public class GoodsCategoryController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    public List<GoodsCategory> search() {
        return goodsService.getCategories();
    }

}
