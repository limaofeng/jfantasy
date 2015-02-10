package com.fantasy.mall.goods.ws;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.mall.goods.ws.dto.GoodsCategoryDTO;
import com.fantasy.mall.goods.ws.dto.GoodsDTO;
import com.fantasy.mall.goods.ws.dto.GoodsPagerResult;

public interface IGoodsService {

    /**
     * 商品检索
     *
     * @param pager   分页对象(每页显示条数，最大数据条数，当前页码，总页数..具体看pager对象)
     * @param filters 过滤条件(比较符+该字段类型+字段名称..具体看PropertyFilter对象)
     * @return pager对象(pager中PageItems是返回数据)
     */
    public GoodsPagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters);


    /**
     * 根据商品ID查询商品详细信息
     *
     * @param id 商品ID
     * @return {GoodsDTO}
     */
    public GoodsDTO getGoodsById(Long id);

    /**
     * 通过分类标示查询商品分类
     *
     * @param sign 商品分类编码
     * @return {CategoryDTO}
     */
    public GoodsCategoryDTO getGoodsCategoryBySign(String sign);

    /**
     * 全部商品分类信息
     *
     * @return GoodsCategory商品分类数组
     */
    public GoodsCategoryDTO[] categories();

    /**
     * 检索商品，返回指定条数的数据
     *
     * @param filters 过滤条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @param size    返回数据条数
     * @return {GoodsDTOs}
     */
    public GoodsDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size);


}
