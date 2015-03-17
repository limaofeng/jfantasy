package com.fantasy.mall.goods.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.goods.bean.Brand;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.service.BrandService;
import com.fantasy.mall.goods.service.GoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 品牌管理
 */
public class BrandAction extends ActionSupport {

    private static final long serialVersionUID = -3032805252418268707L;

    @Autowired
    private BrandService brandService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 品牌功能进入页面
     *
     * @return {string}
     */
    public String index(Pager<Brand> pager, List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 品牌查询
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return {string}
     */
    public String search(Pager<Brand> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, brandService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 品牌保存
     *
     * @param brand 品牌
     * @return {string}
     */
    public String save(Brand brand) {
        this.attrs.put(ROOT, brandService.save(brand));
        return JSONDATA;
    }


    /**
     * 品牌编辑
     *
     * @param id 品牌id
     * @return {string}
     */
    public String edit(Long id, List<PropertyFilter> filters) {
        List<GoodsCategory> categories = goodsService.getCategories();
        List<GoodsCategory> rootCategories = ObjectUtil.filter(categories, " parent == null ");
        // 默认选择根目录的第一个分类
        if (ObjectUtil.find(filters, "filterName", "EQL_category.id") == null) {
            if (!rootCategories.isEmpty()) {
                filters.add(new PropertyFilter("EQL_category.id", rootCategories.get(0).getId().toString()));
            }
        }
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQL_category.id");
        if (filter != null) {
            this.attrs.put("category", ObjectUtil.find(rootCategories, "id", filter.getPropertyValue(String.class)));
        }
        // 全部分类
        this.attrs.put("categorys", categories);
        this.attrs.put("brand", brandService.getBrand(id));
        return SUCCESS;
    }

    /**
     * 批量删除
     *
     * @param ids 品牌ids
     * @return {string}
     */
    public String delete(Long[] ids) {
        this.brandService.deleteBrand(ids);
        return JSONDATA;
    }

    /**
     * 品牌查看
     *
     * @param id 品牌id
     * @return {string}
     */
    public String view(Long id) {
        this.attrs.put("brand", brandService.getBrand(id));
        return SUCCESS;
    }


    /**
     * 查询分类树
     *
     * @param filters 过滤条件
     * @return {string}
     */
    public String add(List<PropertyFilter> filters) {
        List<GoodsCategory> categories = goodsService.getCategories();
        List<GoodsCategory> rootCategories = ObjectUtil.filter(categories, " parent == null ");
        // 默认选择根目录的第一个分类
        if (ObjectUtil.find(filters, "filterName", "EQL_category.id") == null) {
            if (!rootCategories.isEmpty()) {
                filters.add(new PropertyFilter("EQL_category.id", rootCategories.get(0).getId().toString()));
            }
        }
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQL_category.id");
        if (filter != null) {
            this.attrs.put("category", ObjectUtil.find(rootCategories, "id", filter.getPropertyValue(String.class)));
        }
        // 全部分类
        this.attrs.put("categorys", categories);
        return SUCCESS;
    }
}
