package com.fantasy.mall.delivery.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hebo on 2014/8/13.
 */
public class ShippingAction extends ActionSupport {

    @Autowired
    private ShippingService shippingService;

    /**
     * 发货记录首页
     * @param pager
     * @param filters
     * @return
     */
    public String index(Pager<Shipping> pager,List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     *
     * 发货记录查询
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Shipping> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.shippingService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 发货记录详情
     *
     * @param id
     * @return
     */
    public String view(Long id) {
        this.attrs.put("shipping", this.shippingService.get(id));
        return SUCCESS;
    }

    /**
     * 发货记录删除
     * @param ids
     * @return
     */
    public String delete(Long[] ids){
        this.shippingService.delete(ids);
        return JSONDATA;
    }
}
