package com.fantasy.cms.ws;

import com.fantasy.cms.ws.dto.ArticleCategoryDTO;
import com.fantasy.cms.ws.dto.ArticleDTO;
import com.fantasy.cms.ws.dto.ArticlePagerResult;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;

public interface IArticleService {

	/**
	 * 文章检索
	 * 
	 * @param pager
	 *            分页对象(每页显示条数，最大数据条数，当前页码，总页数..具体看pager对象)
	 * @param filters
	 *            过滤条件(比较符+该字段类型+字段名称..具体看PropertyFilter对象)
	 * @return ArticlePagerResult对象(ArticlePagerResult中PageItems是返回数据)
	 */

	public ArticlePagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters);

	/**
	 * 文章栏目信息
	 * 
	 * @return ArticleCategory栏目数组
	 */
	public ArticleCategoryDTO[] categorys();

	/**
	 * 检索文章，返回指定条数的数据
	 * 
	 * @param filters
	 *            过滤条件
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            排序方向
	 * @param size
	 *            返回数据条数
	 * @return {ArticleDTO}
	 */
	public ArticleDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size);


    /**
     * 根据ID查询单篇文章
     * @param id  文章ID
     * @return  文章对象ArticleDTO
     */
    public ArticleDTO findArticleById(Long id);


    /**
     * 根据分类code查询当前分类的下级
     * @param code  分类编码 code
     * @return   分类对象数组
     */
    public ArticleCategoryDTO[] getArticleCategoryDtoByCode(String code);

}
