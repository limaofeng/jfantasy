package com.fantasy.mall.goods.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.mall.goods.service.ProductService;

public class ProductAction extends ActionSupport {

	private static final long serialVersionUID = -3032805252418268707L;

	@Resource(name="goods.productService")
	private ProductService productService;
	
	@Resource
	private GoodsService goodsService;

	/**
	 * 商品功能进入页面
	 * @功能描述 
	 * @return
	 */
	public String index(String id) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQL_goods.id", id));
		this.attrs.put("products", this.productService.find(filters, "createTime", "desc"));
		return SUCCESS;
	}
	

	/**
	 * 商品查询
	 * @功能描述
	 * @param filters
	 * @return
	 */
	public String search(List<PropertyFilter> filters) {
		this.attrs.put(ROOT, this.productService.find(filters, "createTime", "desc"));
		return JSONDATA;
	}
	/**
	 * 添加 
	 * @param id
	 * @return
	 */
	public String add(Long id){
		this.attrs.put("goods", goodsService.getGoods(id));
		return SUCCESS;
	}
	
	/**
	 * 保存
	 * @param product
	 * @return
	 */
	public String save(Product product) {
		this.attrs.put(ROOT, productService.save(product));
		return JSONDATA;
	}
	/**
	 * 修改
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("product", productService.getProduct(id));
		return SUCCESS;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.productService.delete(ids);
		return JSONDATA;
	}
	
	/**
	 * 批量上架
	 * @param ids
	 * @return
	 */
	public String up(Long[] ids) {
		this.productService.upProducts(ids);
		return JSONDATA;
	}
	
	/**
	 * 批量下架
	 * @param ids
	 * @return
	 */
	public String down(Long[] ids) {
		this.productService.downProducts(ids);
		return JSONDATA;
	}
	
	/**
	 * 补货
	 * @param id
	 * @return
	 */
	public String replenish(Long id) {
		this.attrs.put("product", productService.getProduct(id));
		return SUCCESS;
	}
	
	/**
	 * 补货保存
	 * @param product
	 * @return
	 */
	public String replenishSave(Product product){
		Product productOld = productService.getProduct(product.getId());
		productOld.setStore(product.getStore());
		productOld.setFreezeStore(product.getFreezeStore());
		this.attrs.put(ROOT, productService.save(productOld));
		return JSONDATA;
	}
	
	/**
	 * 显示
	 */
	public String view(Long id) {
		this.attrs.put("product", productService.getProduct(id));
		return SUCCESS;
	}
}
