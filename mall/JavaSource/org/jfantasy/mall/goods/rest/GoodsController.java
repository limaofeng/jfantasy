package org.jfantasy.mall.goods.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.goods.bean.Goods;
import org.jfantasy.mall.goods.bean.GoodsCategory;
import org.jfantasy.mall.goods.bean.Product;
import org.jfantasy.mall.goods.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "mall-goods", description = "商品")
@RestController
@RequestMapping("/mall/goodses")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Goods> search(Pager<Goods> pager, List<PropertyFilter> filters) {
        return goodsService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Goods view(@PathVariable("id") Long id) {
        return goodsService.get(id);
    }

    @RequestMapping(value = "/{id}/category", method = RequestMethod.GET)
    @ResponseBody
    public GoodsCategory category(@PathVariable("id") Long id) {
        return goodsService.get(id).getCategory();
    }

    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> products(@PathVariable("id") Long id) {
        return goodsService.get(id).getProducts();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Goods create(@RequestBody Goods goods) {
        return goodsService.save(goods);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    public Goods update(@PathVariable("id") Long id, @RequestBody Goods goods) {
        goods.setId(id);
        return goodsService.save(goods);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        goodsService.deleteGoods(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        goodsService.deleteGoods(id);
    }

}
