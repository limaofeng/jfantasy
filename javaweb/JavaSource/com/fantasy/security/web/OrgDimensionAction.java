package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.service.OrgDimensionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hebo on 2015/1/6.
 *
 */
public class OrgDimensionAction extends ActionSupport{

    @Resource
    private OrgDimensionService orgDimensionService;

    /**
     * 首页
     * @param pager pager
     * @param filters filters
     * @return String
     */
    public String index(Pager<OrgDimension> pager, List<PropertyFilter> filters){
        this.search(pager,filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 查询方法
     * @param pager pager
     * @param filters filters
     * @return String
     */
    public String search(Pager<OrgDimension> pager, List<PropertyFilter> filters){
        if(StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT,this.orgDimensionService.findPager(pager,filters));
        return JSONDATA;
    }

    /**
     * 保存
     * @param orgDimension orgDimension
     * @return  String
     */
    public String save(OrgDimension orgDimension){
        this.attrs.put(ROOT,this.orgDimensionService.save(orgDimension));
        return JSONDATA;
    }

    /**
     * 根据ID查询对象
     * @param id id
     * @return String
     */
    public String get(String id){
       this.attrs.put("orgDimension",this.orgDimensionService.get(id));
        return SUCCESS;
    }

    /**
     * 删除
     * @param ids ids
     * @return String
     */
    public String delete(String... ids){
        this.orgDimensionService.delete(ids);
        return JSONDATA;
    }
}
