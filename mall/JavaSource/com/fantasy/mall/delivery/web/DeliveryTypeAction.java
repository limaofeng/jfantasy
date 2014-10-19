package com.fantasy.mall.delivery.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.service.DeliveryService;

/**
 * 配送获取
 * 
 */
public class DeliveryTypeAction extends ActionSupport {

	private static final long serialVersionUID = -2840908152069230972L;

	@Resource
	private DeliveryService deliveryService;

	/**
	 * 配送首页
	 * 
	 * @return
	 */
	public String index() {
		this.search(new Pager<DeliveryType>(), new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 配送列表查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<DeliveryType> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, this.deliveryService.findDeliveryTypePager(pager, filters));
		return JSONDATA;
	}

	/**
	 * 配送保存
	 * 
	 * @param deliveryType
	 * @return
	 */
	public String save(DeliveryType deliveryType) {
		this.attrs.put(ROOT, this.deliveryService.save(deliveryType));
		return JSONDATA;
	}

	/**
	 * 配送修改
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("deliveryType", this.deliveryService.getDeliveryType(id));
		return SUCCESS;
	}

	/**
	 * 配送删除
	 * 
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.deliveryService.deleteDeliveryType(ids);
		return JSONDATA;
	}

}
