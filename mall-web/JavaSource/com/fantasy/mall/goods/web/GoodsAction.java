package com.fantasy.mall.goods.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.rest.RestActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GoodsAction extends RestActionSupport {

    @Autowired
    private GoodsService goodsService;

    public String search(Pager<Goods> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.goodsService.findPager(pager, filters));
        return SUCCESS;
    }

    public String view(Long id) {
        this.attrs.put(ROOT, this.goodsService.get(id));
        return SUCCESS;
    }

    public String create(Goods goods) {
        this.attrs.put(ROOT, this.goodsService.save(goods));
        return SUCCESS;
    }

    public String delete(Long... id) {
        this.goodsService.deleteGoods(id);
        return SUCCESS;
    }

    public String update(Goods goods) {
        this.attrs.put(ROOT, this.goodsService.save(goods));
        return SUCCESS;
    }

}
