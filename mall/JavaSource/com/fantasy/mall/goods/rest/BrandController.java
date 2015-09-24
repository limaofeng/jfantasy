package com.fantasy.mall.goods.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.goods.bean.Brand;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.service.BrandService;
import com.fantasy.mall.goods.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "mall-brands", description = "商品品牌")
@RestController
@RequestMapping("/mall/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Brand> search(Pager<Brand> pager, List<PropertyFilter> filters) {
        return this.brandService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Brand view(@PathVariable("id") Long id) {
        return this.brandService.get(id);
    }

    @RequestMapping(value = "/{id}/goodses", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Goods> goodses(@PathVariable("id") Long id, Pager<Goods> pager, List<PropertyFilter> filters) {
        return this.goodsService.findPager(pager, filters);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Brand create(@RequestBody Brand brand) {
        return this.brandService.save(brand);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Brand update(@PathVariable("id") Long id, @RequestBody Brand brand) {
        brand.setId(id);
        return this.brandService.save(brand);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.brandService.delete(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... id) {
        this.brandService.delete(id);
    }

}
