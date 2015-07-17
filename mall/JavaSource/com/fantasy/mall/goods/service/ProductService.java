package com.fantasy.mall.goods.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.mall.goods.dao.ProductDao;
import com.fantasy.mall.order.service.OrderException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 货品 service
 */
@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductDao productDao;

	public Pager<Product> findPager(Pager<Product> pager, List<PropertyFilter> filters) {
		return this.productDao.findPager(pager, filters);
	}

	public List<Product> find(List<PropertyFilter> filters, String orderBy, String order) {
		return this.productDao.find(filters, orderBy, order);
	}

	public Product get(String sn) {
		return this.productDao.findUnique(Restrictions.eq("sn", sn));
	}

	public Product get(Long id) {
		return this.productDao.get(id);
	}

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setStore(0);
			product.setFreezeStore(0);
			product.setMarketable(false);
			product.setIsDefault(false);
		}
		return this.productDao.save(product);
	}

	public Product getProduct(Long id) {
		return this.productDao.get(id);
	}

	/**
	 * 批量上架
	 * 
	 */
	public void upProducts(Long[] ids) {
		for (Long id : ids) {
			Product product = this.productDao.get(id);
			product.setMarketable(true);
			this.save(product);
		}
	}

	/**
	 * 批量下架
	 * 
	 * @param ids
	 */
	public void downProducts(Long[] ids) {
		for (Long id : ids) {
			Product product = this.productDao.get(id);
			product.setMarketable(false);
			this.save(product);
		}
	}

	public void delete(Long[] ids) {
		for (Long id : ids) {
			this.productDao.delete(id);
		}
	}

	/**
	 * 调整存货数量
	 * 
	 * @功能描述
	 * @param id
	 *            货物id
	 * @param number
	 *            调整数量
	 */
	public void replenish(Long id, Integer number) {
		Product product = this.productDao.get(id);
		product.setStore(product.getStore() + number);
		this.productDao.save(product);
	}

	/**
	 * 订单生成时,更新商品的存货数量
	 * 
	 * @功能描述
	 * @param id
	 * @param quantity
	 */
	public void freezeStore(Long id, Integer quantity) {
		Product product = this.productDao.get(id);
		if (quantity > 0 && (product.getStore() - product.getFreezeStore()) <= quantity) {
			throw new OrderException("商品库存不足,不能购买!");
		}
		product.setFreezeStore(product.getFreezeStore() + quantity);
		this.productDao.save(product);
	}

	public static Product getProductBySn(String sn) {
		return copyProduct(SpringContextUtil.getBeanByType(ProductService.class).get(sn));
	}

	public static Product copyProduct(Product orig) {
        return null;
		//TODO return ObjectUtil.copy(new Product(), orig, "stocks", "goods.favoriteMembers", "goods.brand", "goods.comments", "goods.products", "goods.category", "goods.flashSaleItems", "cartItems", "orderItems", "deliveryItems","warningSettings");
	}

}
