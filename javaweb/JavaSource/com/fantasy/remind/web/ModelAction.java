package com.fantasy.remind.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.remind.bean.Model;
import com.fantasy.remind.service.ModelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 公告 action
 */

public class ModelAction extends ActionSupport {

    @Resource
    private ModelService modelService;

    public String index(){
        this.search(new Pager<Model>(),new ArrayList<PropertyFilter>());
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Model> pager,List<PropertyFilter> filters){
        if(pager.getOrderBy()==null){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT,this.modelService.findPager(pager,filters));
        return JSONDATA;
    }

    public String save(Model model){
        this.modelService.save(model);
        return JSONDATA;
    }
    public String edit(Long id){
       this.attrs.put("model",this.modelService.get(id));
       return SUCCESS;
    }

    public String view(Long id){
        this.attrs.put("model",this.modelService.get(id));
        return SUCCESS;
    }

    public String delete(Long[] ids){
        this.modelService.delete(ids);
        return JSONDATA;
    }




}
