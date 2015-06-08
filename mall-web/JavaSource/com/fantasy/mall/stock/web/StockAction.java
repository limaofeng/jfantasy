package com.fantasy.mall.stock.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.mall.goods.service.ProductService;
import com.fantasy.mall.stock.bean.Stock;
import com.fantasy.mall.stock.service.StockService;

/**
 *@Author lsz
 *@Date 2013-11-28 下午5:06:38
 *
 */
public class StockAction extends ActionSupport {
	private static final long serialVersionUID = 4849845824503845413L;
	
	@Autowired
	private StockService stockService;
	@Autowired
	private ProductService productService;
	
	/**
	 * 商品功能进入页面
	 * @功能描述 
	 * @return
	 */
	public String productIndex() {
		this.productSearch(new Pager<Product>(), new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	

	/**
	 * 商品查询
	 * @功能描述
	 * @param filters
	 * @return
	 */
	public String productSearch(Pager<Product> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, this.productService.findPager(pager, filters));
		return JSONDATA;
	}
	
	/**
	 * 首页
	 * @return
	 */
	public String index(Pager<Stock> pager,List<PropertyFilter> filters){
		this.search(pager,filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	/**
	 * 搜索
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Stock> pager,List<PropertyFilter> filters){
		this.attrs.put(ROOT, this.stockService.findPager(pager, filters));
		return JSONDATA;
	}
	
	/**
	 * 添加,减少库存
	 * @param productId
	 * @return
	 */
	public String change(Long productId,String change){
		this.attrs.put("product", this.productService.getProduct(productId));
		this.attrs.put("change", change);
		return SUCCESS;
	}
	
	/**
	 * 库存变量增加
	 * @param stock
	 * @return
	 */
	public String save(Stock stock,String change){
		if(change.equals("plus")){
			stock.setStatus(true);
		}else if(change.equals("minus")){
			stock.setStatus(false);
		}
		this.attrs.put("stock", this.stockService.save(stock));
		return JSONDATA;
	}

	/**
	 * 库存预警保存
	 * @param id
	 * @param count
	 * @return
	 */
	public String warnSave(String expression,Long... ids){
		this.stockService.productWarnSave(expression,ids);
		return JSONDATA;
	}

}

