package com.fantasy.mall.delivery.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.bean.Reship;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.service.DeliveryService;

import javax.annotation.Resource;

/**
 * 配送管理 <br/>
 * User: Administrator <br/>
 * Date: 13-12-4 <br/>
 */
public class DeliveryAction extends ActionSupport {

	private static final long serialVersionUID = -28409081520692372L;

	@Resource
	private DeliveryService deliveryService;

	/**
	 * 添加发货信息
	 * 
	 * @param shipping
	 * @return
	 */
	public String shipping(Shipping shipping) {
		deliveryService.save(shipping);
		return JSONDATA;
	}

	/**
	 * 添加退货信息
	 * 
	 * @param shipping
	 * @return
	 */
	public String reship(Reship reship) {
		deliveryService.save(reship);
		return SUCCESS;
	}

	/**
	 * 获取发货详情
	 * @param id
	 * @return
	 */
	public String view(Long id) {
		this.attrs.put("shipping", this.deliveryService.getShipping(id));
		return SUCCESS;
	}
	

}
