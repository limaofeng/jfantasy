package com.fantasy.cms.rest;

import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @apiDefine paramArticleCategory
 * @apiParam {String} code  编码
 * @apiParam {String} name  名称
 * @apiParam {String} layer  层级
 * @apiParam {String} path  路径
 * @apiParam {String} description  描述
 * @apiParam {Date} sort  排序字段
 * @apiParam {String} parent_code  上级栏目编码
 * @apiVersion 3.3.8
 */

/**
 * @apiDefine returnArticleCategory
 * @apiSuccess {String} code  编码
 * @apiSuccess {String} name  名称
 * @apiSuccess {String} layer  层级
 * @apiSuccess {String} path  路径
 * @apiSuccess {String} description  描述
 * @apiSuccess {Date} sort  排序字段
 * @apiSuccess {String} parent  上级栏目编码
 * @apiVersion 3.3.8
 */
@RestController
@RequestMapping("/cms/categorys")
public class ArticleCategoryController {


    @Autowired
    private CmsService cmsService;

    /**
     * @api {get} /cms/categorys   查询文章分类
     * @apiVersion 3.3.8
     * @apiName searchArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 筛选文章分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/categorys?currentPage=1&EQS_code=article
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse GeneralError
     * @param pager 分页对象
     * @param filters 过滤条件对象
     */
    @RequestMapping(method = RequestMethod.GET)
    public Pager<ArticleCategory> search(Pager<ArticleCategory> pager, List<PropertyFilter> filters) {
        return this.cmsService.findCategoryPager(pager, filters);
    }

    /**
     * @api {get} /cms/categorys/:code   获取文章分类
     * @apiVersion 3.3.8
     * @apiName getArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 获取文章分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/categorys/root
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
    @RequestMapping("/{code}")
    public ArticleCategory view(@PathVariable("code") String code) {
        return this.cmsService.get(code);
    }

    /**
     * @api {post} /cms/categorys   添加文章分类
     * @apiVersion 3.3.8
     * @apiName createArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 添加文章分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "code=test&name=测试&parent.code=root" http://localhost/cms/categorys
     * @apiUse paramArticleCategory
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    public ArticleCategory create(ArticleCategory category) {
        return this.cmsService.save(category);
    }

    /**
     * @api {delete} /cms/categorys/:code   删除文章分类
     * @apiVersion 3.3.8
     * @apiName deleteArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 删除文章分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/categorys/root
     * @apiUse GeneralError
     */
    @RequestMapping(value="/{code}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable("code") String code) {
        this.cmsService.delete(code);
    }

    /**
     * @api {batchDelete} /cms/categorys/:code   批量删除分类
     * @apiVersion 3.3.5
     * @apiName batchDeleteArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 批量删除分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE -d "code=a&code=b" http://localhost/cms/categorys
     * @apiUse GeneralError
     * @param code:a
     * @param code:b
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(String... code) {
        this.cmsService.delete(code);
    }

    /**
     * @api {put} /cms/categorys/:code   更新文章分类
     * @apiVersion 3.3.8
     * @apiName updateArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 更新文章分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/categorys/root
     * @apiUse paramArticleCategory
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
    @RequestMapping(value="/{code}",method = {RequestMethod.PUT})
    public ArticleCategory update(@PathVariable("code") String code,ArticleCategory category) {
        category.setCode(code);
        return this.cmsService.save(category);
    }

}
