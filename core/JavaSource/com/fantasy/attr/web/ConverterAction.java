package com.fantasy.attr.web;

import com.fantasy.attr.storage.bean.Converter;
import com.fantasy.attr.storage.service.ConverterService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 转换器
 * @author mingliang
 *
 */
public class ConverterAction extends ActionSupport {

    @Autowired
    private ConverterService converterService;
    /**
     * 转换器首页
     *
     * @功能描述
     * @return
     */
	public String index(Pager<Converter> pager, List<PropertyFilter> filters) {
		this.search(pager, filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @功能描述
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Converter> pager, List<PropertyFilter> filters) {
		if (StringUtil.isBlank(pager.getOrderBy())) {
			pager.setOrderBy("id");
			pager.setOrders(Pager.Order.desc);
		}
		this.attrs.put(ROOT, converterService.findPager(pager, filters));
		return JSONDATA;
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	public String save(Converter converter) {
		this.attrs.put(ROOT, this.converterService.save(converter));
		return JSONDATA;
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("converter", this.converterService.get(id));
		return SUCCESS;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.converterService.delete(ids);
		return JSONDATA;
	}

    /**
     * 详情
     * @param id
     * @return
     */
    public String view(Long id){
        this.attrs.put("converter", this.converterService.get(id));
        return SUCCESS;
    }
}
