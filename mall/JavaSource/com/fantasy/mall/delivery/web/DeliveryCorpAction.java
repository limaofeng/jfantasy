package com.fantasy.mall.delivery.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.delivery.bean.DeliveryCorp;
import com.fantasy.mall.delivery.service.DeliveryService;

/**
 * 物流公司管理
 * 
 */
public class DeliveryCorpAction extends ActionSupport {

	private static final long serialVersionUID = -2840908152069230972L;

	@Autowired
	private DeliveryService deliveryService;

	/**
	 * 物流首页
	 */
	public String index() {
		this.search(new Pager<DeliveryCorp>(), new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 物流查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<DeliveryCorp> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, this.deliveryService.findDeliveryCorpPager(pager, filters));
		return JSONDATA;
	}

	/**
	 * 物流保存
	 * 
	 * @param deliveryCorp
	 * @return
	 */
	public String save(DeliveryCorp deliveryCorp) {
		this.attrs.put(ROOT, this.deliveryService.save(deliveryCorp));
		return JSONDATA;
	}

	/**
	 * 物流编辑
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("deliveryCorp", this.deliveryService.getDeliveryCorp(id));
		return SUCCESS;
	}

	/**
	 * 物流批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.deliveryService.deleteDeliveryCorp(ids);
		return JSONDATA;
	}

}
