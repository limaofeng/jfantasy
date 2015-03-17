package com.fantasy.mall.sales.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.sales.bean.Sales;
import com.fantasy.mall.sales.dao.SalesDao;

@Service
@Transactional
public class SalesService {
	@Autowired
	private SalesDao salesDao;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ProductService productService;

	public Sales addSales(Sales.Type type, String sn, TimeUnit timeUnit, String time, Integer quantity, BigDecimal amount) {
		Sales sales = salesDao.findUnique(Restrictions.eq("type", type), Restrictions.eq("sn", sn), Restrictions.eq("timeUnit", timeUnit), Restrictions.eq("time", time));
		if (sales == null) {
			sales = new Sales();
			sales.setType(type);
			sales.setSn(sn);
			sales.setTimeUnit(timeUnit);
			sales.setTime(time);
			if (Sales.Type.goods == type) {
				sales.setPath(goodsService.get(sn).getCategory().getPath());
			} else if (Sales.Type.product == type) {
				sales.setPath(productService.get(sn).getGoods().getCategory().getPath());
			}
			sales.setQuantity(0);
			sales.setAmount(BigDecimal.valueOf(0));
		}
		if (TimeUnit.day == timeUnit) {
			sales.setQuantity(sales.getQuantity() + quantity);
			sales.setAmount(sales.getAmount().add(amount));
		} else {
			sales.setQuantity(quantity);
			sales.setAmount(amount);
		}
		this.salesDao.save(sales);
		return sales;
	}

	/**
	 * 获取商品近30天的销量
	 * 
	 * @param sn
	 * @return
	 */
	public Integer nearly30DaysOfSales(String sn) {
		Date now = DateUtil.now();
		return ObjectUtil.defaultValue(this.getSaleCount(Sales.Type.goods, sn, TimeUnit.day, DateUtil.format(DateUtil.add(now, Calendar.MONTH, -30), "yyyyMMdd"), DateUtil.format(now, "yyyyMMdd")), 0);
	}

	/**
	 * 获取销售量
	 * 
	 * @param start
	 *            统计开始时间
	 * @param end
	 *            统计结束时间
	 * @param timeUnit
	 *            类型
	 * @param sn
	 *            对应的编码
	 * @return
	 * @功能描述
	 */
	public Integer getSaleCount(Sales.Type type, String sn, TimeUnit timeUnit, String start, String end) {
		return this.salesDao.getSaleCount(type, sn, timeUnit, start, end);
	}

	/**
	 * 销量排行
	 * @功能描述 
	 * @param criterions
	 * @param satrt
	 * @param size
	 * @return
	 */
	public List<Sales> sellingRanking(Criterion[] criterions, int satrt, int size) {
		return this.salesDao.find(criterions, "quantity", "desc", satrt, size);
	}

	/**
	 * 查询商品统计
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public Pager<Sales> findPager(Pager<Sales> pager, List<PropertyFilter> filters) {
		return this.salesDao.findPager(pager, filters);
	}

	public List<Sales> find(Criterion[] criterions) {
		return this.salesDao.find(criterions);
	}

	public List<Sales> getSalesBySn(Criterion... criterions) {
		return this.salesDao.getSalesBySn(criterions);
	}

	public List<Sales> statistics(Criterion[] criterions, Map<String, Projection> projections) {
		return this.salesDao.statistics(criterions, projections);
	}

	public <E> List<E> statistics(Criterion[] criterions, Map<String, Projection> projections, Class<E> entity) {
		return this.salesDao.statistics(criterions, projections, entity);
	}

	public List<Sales> statistics(List<PropertyFilter> filters, Map<String, Projection> projections) {
		return this.salesDao.statistics(filters, projections);
	}

	public <E> List<E> statistics(List<PropertyFilter> filters, Map<String, Projection> projections, Class<E> entity) {
		return this.salesDao.statistics(filters, projections, entity);
	}
}
