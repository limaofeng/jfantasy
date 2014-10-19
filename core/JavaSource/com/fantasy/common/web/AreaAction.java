package com.fantasy.common.web;

import com.fantasy.common.bean.Area;
import com.fantasy.common.service.AreaService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 *@Author lsz
 *@Date 2013-11-21 下午1:30:15
 *
 */
public class AreaAction extends ActionSupport {

    private static final long serialVersionUID = 7102911556906914170L;

    @Resource
    private AreaService areaService;

    /**
     * 首页
     * @return
     */
    public String index(Pager<Area> pager,List<PropertyFilter> filters){
        filters.add(new PropertyFilter("NULL_parent"));
        this.search(pager, filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 分页
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Area> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT, this.areaService.findPager(pager, filters));
        return JSONDATA;
    }


    /**
     * 保存
     * @param area
     * @return
     */
    public String save(Area area){
        this.attrs.put(ROOT, this.areaService.save(area));
        return JSONDATA;
    }

    public String list(String parentId){
        this.attrs.put(ROOT, AreaService.list(parentId));
        return JSONDATA;
    }

    /**
     * 修改
     * @param id
     * @return
     */
    public String edit(String id){
        this.attrs.put("area", this.areaService.get(id));
        return SUCCESS;
    }

    /**
     * 删除
     */
    public String delete(String[] ids){
        this.areaService.delete(ids);
        return JSONDATA;
    }

}

