package com.fantasy.swp.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.swp.bean.Trigger;
import com.fantasy.swp.service.TriggerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TriggerAction extends ActionSupport{


    @Autowired
    private TriggerService triggerService;

    /**
     * 首页
     * @return
     */
    public String index( Pager<Trigger> pager,List<PropertyFilter> filters){
        this.search(pager,filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 搜索
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Trigger> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT,this.triggerService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 保存
     * @param trigger
     * @return
     */
    public String save(Trigger trigger){
        this.triggerService.save(trigger);
        return JSONDATA;
    }
    /**
     * 修改
     * @param id
     * @return
     */
    public String edit(Long id){
        this.attrs.put("trigger", this.triggerService.get(id));
        return SUCCESS;
    }

    public String delete(Long[] ids){
        this.triggerService.delete(ids);
        return JSONDATA;
    }
}
