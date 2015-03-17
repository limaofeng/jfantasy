package com.fantasy.attr.web;

import java.util.List;

import javax.annotation.Resource;

import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.service.AttributeTypeService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;

/**
 * 商品属性类型
 * @author mingliang
 *
 */
public class AttributeTypeAction extends ActionSupport {

	private static final long serialVersionUID = -3032805252418268707L;
	@Autowired
	private AttributeTypeService attributeTypeService;

	/**
	 * 商品属性类型进入页面
	 * 
	 * @功能描述
	 * @return
	 */
	public String index(Pager<AttributeType> pager, List<PropertyFilter> filters) {
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
	public String search(Pager<AttributeType> pager, List<PropertyFilter> filters) {
		if (StringUtil.isBlank(pager.getOrderBy())) {
			pager.setOrderBy("id");
			pager.setOrder(Pager.Order.desc);
		}
		this.attrs.put(ROOT, attributeTypeService.findPager(pager, filters));
		return JSONDATA;
	}

	/**
	 * 保存
	 * 
	 * @param article
	 * @return
	 */
	public String save(AttributeType attributeType) {
		this.attrs.put(ROOT, attributeTypeService.save(attributeType));
		return JSONDATA;
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("attributeType", this.attributeTypeService.get(id));
		return SUCCESS;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.attributeTypeService.delete(ids);
		return JSONDATA;
	}
	
}
