package com.fantasy.mall.delivery.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.bean.Reship;
import com.fantasy.mall.delivery.service.ReshipService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hebo on 2014/8/13.
 */
public class ReshipAction extends ActionSupport {

    @Autowired
    private ReshipService reshipService;

    /**
     * 退货记录首页
     * @param pager
     * @param filters
     * @return
     */
    public String index(Pager<Reship> pager,List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     *
     * 退货记录查询
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Reship> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, this.reshipService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 退货记录详情
     *
     * @param id
     * @return
     */
    public String view(Long id) {
        this.attrs.put("reship", this.reshipService.get(id));
        return SUCCESS;
    }

    /**
     * 退货记录删除
     * @param ids
     * @return
     */
    public String delete(Long[] ids){
        this.reshipService.delete(ids);
        return JSONDATA;
    }
}
