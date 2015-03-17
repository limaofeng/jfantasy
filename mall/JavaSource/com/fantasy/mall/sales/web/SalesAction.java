package com.fantasy.mall.sales.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.mall.sales.bean.Sales;
import com.fantasy.mall.sales.bean.Sales.Type;
import com.fantasy.mall.sales.service.SalesService;

/**
 * @Author lsz
 * @Date 2013-12-26 上午11:51:51
 * 
 */
public class SalesAction extends ActionSupport {

	@Autowired
	private SalesService salesService;

	private static final long serialVersionUID = 3966151832335206089L;

	public String goods() {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQE_type", "goods"));
		filters.add(new PropertyFilter("EQE_timeUnit", "day"));
		
		Date now = DateUtil.now();
		filters.add(new PropertyFilter("GES_time", DateUtil.format(DateUtil.first(now,Calendar.DAY_OF_WEEK), "yyyyMMdd")));
		filters.add(new PropertyFilter("LES_time", DateUtil.format(DateUtil.last(now,Calendar.DAY_OF_WEEK), "yyyyMMdd")));
		
		this.search(new Pager<Sales>(), filters);
		this.attrs.put("pager", OgnlUtil.getInstance().getValue("pager", this.attrs.get(ROOT)));
		this.attrs.put("statistics", OgnlUtil.getInstance().getValue("statistics", this.attrs.get(ROOT)));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	public String order() {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQE_type", "order"));
		filters.add(new PropertyFilter("EQE_timeUnit", "day"));
		
		Date now = DateUtil.now();
		filters.add(new PropertyFilter("GES_time", DateUtil.format(DateUtil.first(now,Calendar.DAY_OF_WEEK), "yyyyMMdd")));
		filters.add(new PropertyFilter("LES_time", DateUtil.format(DateUtil.last(now,Calendar.DAY_OF_WEEK), "yyyyMMdd")));
		
		this.search(new Pager<Sales>(), filters);
		this.attrs.put("pager", OgnlUtil.getInstance().getValue("pager", this.attrs.get(ROOT)));
		this.attrs.put("statistics", OgnlUtil.getInstance().getValue("statistics", this.attrs.get(ROOT)));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	public String search(Pager<Sales> pager, List<PropertyFilter> filters) {
		pager = this.salesService.findPager(pager, filters);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("pager", pager);
		Map<String, Projection> projections = new HashMap<String, Projection>();
		
		projections.put("quantity", Projections.count("sn"));
		projections.put("amount", Projections.sum("amount"));
		
		List<Sales> salesList = this.salesService.statistics(filters, projections);
		Sales statistics = salesList.isEmpty() ? new Sales() : salesList.get(0);
		statistics.setType(ObjectUtil.find(filters, "filterName", "EQE_type").getPropertyValue(Type.class));
		statistics.setTimeUnit(ObjectUtil.find(filters, "filterName", "EQE_timeUnit").getPropertyValue(TimeUnit.class));
		statistics.setStartTime(ObjectUtil.find(filters, "filterName", "GES_time").getPropertyValue(String.class));
		statistics.setEndTime(ObjectUtil.find(filters, "filterName", "LES_time").getPropertyValue(String.class));
		root.put("statistics", statistics);
		this.attrs.put(ROOT, root);
		return JSONDATA;
	}

}
