package com.fantasy.system.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.system.bean.*;
import com.fantasy.system.service.DataDictionaryService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class DataDictionaryAction extends ActionSupport {

    @Resource
    private transient DataDictionaryService dataDictionaryService;

    /**
     * 跳转配置项列表
     *
     * @return {String}
     */
    public String index() {
        this.search(new Pager<DataDictionary>(), new ArrayList<PropertyFilter>());
        this.attrs.put("types", this.dataDictionaryService.allTypes());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 配置项分页条件查询
     *
     * @param pager 分页对象
     * @param filters 过滤条件
     * @return {String}
     */
    public String search(Pager<DataDictionary> pager, List<PropertyFilter> filters) {
        pager = this.dataDictionaryService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    /**
     * 保存
     *
     * @功能描述 新增和修改都适用
     * @param dd 数据字典
     * @return {String}
     */
    public String save(DataDictionary dd) {
        this.dataDictionaryService.save(dd);
        this.attrs.put(ROOT, dd);
        return JSONDATA;
    }

    /**
     * 根据Id查询配置项信息
     *
     * @param key 数据字典key
     * @return {String}
     */
    public String edit(DataDictionaryKey key) {
        this.attrs.put("dd", this.dataDictionaryService.get(key));
        return SUCCESS;
    }

    /**
     * 保存配置项分类
     *
     * @param ddt 数据字典分类
     * @return {String}
     */
    public String saveType(DataDictionaryType ddt) {
        this.dataDictionaryService.save(ddt);
        this.attrs.put(ROOT, ddt);
        return JSONDATA;
    }

    public String deletetType(String... codes) {
        this.dataDictionaryService.deleteType(codes);
        return JSONDATA;
    }


    /**
     * 删除配置项
     *
     * @param keys 数据字典 key
     * @return {String}
     */
    public String delete(DataDictionaryKey... keys) {
        this.dataDictionaryService.delete(keys);
        return JSONDATA;
    }


}
