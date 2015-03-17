package com.fantasy.mall.goods.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.system.util.SettingUtil;

import javax.annotation.Resource;
import java.util.List;

public class GoodsAction extends ActionSupport {

    private static final long serialVersionUID = -3032805252418268707L;

    @Autowired
    private GoodsService goodsService;

    public String goods(Pager<Goods> pager, List<PropertyFilter> filters) {
        List<GoodsCategory> categories;
        String rootCode = SettingUtil.getValue("goods");
        if (StringUtil.isNotBlank(rootCode)) {
            categories = goodsService.getCategories(rootCode);
        } else {
            categories = goodsService.getCategories();
        }
        // 默认选择根目录的第一个分类
        if (ObjectUtil.find(filters, "filterName", "EQL_category.id") == null) {
            filters.add(new PropertyFilter("EQL_category.id", categories.isEmpty() ? rootCode : categories.get(0).getId().toString()));
        }
        // 设置当前根
        PropertyFilter filter = ObjectUtil.find(filters, "filterName", "EQL_category.id");
        if (filter != null) {
            this.attrs.put("category", ObjectUtil.find(categories, "id", filter.getPropertyValue(String.class)));
        }
        // 全部分类
        this.attrs.put("categorys", categories);
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 商品功能进入页面
     *
     * @return string
     */
    public String index(Pager<Goods> pager, List<PropertyFilter> filters) {
        PropertyFilter filter;
        if ((filter = ObjectUtil.find(filters, "filterName", "EQL_category.id")) != null) {
            this.attrs.put("category", this.goodsService.getCategory(filter.getPropertyValue(Long.class)));
        } else if ((filter = ObjectUtil.find(filters, "filterName", "EQS_category.sign")) != null) {
            this.attrs.put("category", this.goodsService.getCategoryBySign(filter.getPropertyValue(String.class)));
        }
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 商品查询
     *
     * @param pager   分页条件
     * @param filters 筛选条件
     * @return string
     */
    public String search(Pager<Goods> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, goodsService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 分类保存
     *
     * @param goodsCategory 分类
     * @return string
     */
    public String categorySave(GoodsCategory goodsCategory) {
        this.attrs.put(ROOT, goodsService.save(goodsCategory));
        return JSONDATA;
    }

    /**
     * 分类编辑
     *
     * @param id 分类id
     * @return string
     */
    public String categoryEdit(Long id) {
        this.attrs.put("category", goodsService.getCategory(id));
        return SUCCESS;
    }

    /**
     * 分类删除
     *
     * @param ids 分类ids
     * @return string
     */
    public String categoryDelete(Long[] ids) {
        this.goodsService.deleteCategory(ids);
        return JSONDATA;
    }

    /**
     * 保存商品
     *
     * @param goods 商品对象
     * @return string
     */
    public String goodsSave(Goods goods) {
        this.attrs.put(ROOT, goodsService.save(goods));
        return JSONDATA;
    }

    /**
     * 修改商品
     *
     * @param id 商品id
     * @return string
     */
    public String goodsEdit(Long id, Long categoryId) {
        this.attrs.put("goods", goodsService.getGoods(id));
        return SUCCESS;
    }

    /**
     * 商品上架
     *
     * @param ids 商品ids
     * @return string
     */
    public String goodsUp(Long[] ids) {
        this.goodsService.upGoods(ids);
        return JSONDATA;
    }

    /**
     * 商品下架
     *
     * @param ids 商品ids
     * @return string
     */
    public String goodsDown(Long[] ids) {
        this.goodsService.downGoods(ids);
        return JSONDATA;
    }

    /**
     * 商品删除
     *
     * @param ids 商品ids
     * @return string
     */
    public String goodsDelete(Long[] ids) {
        this.goodsService.deleteGoods(ids);
        return JSONDATA;
    }

    /**
     * 商品数据移动
     *
     * @param ids        商品ids
     * @param categoryId 分类id
     * @return string
     */
    public String moveGoods(Long[] ids, Long categoryId) {
        this.goodsService.moveGoods(ids, categoryId);
        return JSONDATA;
    }

}
