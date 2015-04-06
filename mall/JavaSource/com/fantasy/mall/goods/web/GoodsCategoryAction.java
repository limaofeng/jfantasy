package com.fantasy.mall.goods.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.rest.RestActionSupport;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GoodsCategoryAction extends RestActionSupport{

    @Autowired
    private GoodsService goodsService;

    public String search(Pager<GoodsCategory> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.goodsService.findCategoryPager(pager, filters));
        return SUCCESS;
    }

    public String view(Long id) {
        this.attrs.put(ROOT, this.goodsService.getCategory(id));
        return SUCCESS;
    }

    public String create(GoodsCategory category) {
        this.attrs.put(ROOT, this.goodsService.save(category));
        return SUCCESS;
    }

    public String delete(Long... id) {
        this.goodsService.deleteCategory(id);
        return SUCCESS;
    }

    public String update(GoodsCategory category) {
        this.attrs.put(ROOT, this.goodsService.save(category));
        return SUCCESS;
    }

}
