package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @apiDefine paramArticle
 * @apiParam {String} title  标题
 * @apiParam {String} summary  摘要
 * @apiParam {String} keywords  关键词
 * @apiParam {String} content  正文
 * @apiParam {String} author  作者
 * @apiParam {Date} releaseDate  发布日期
 * @apiParam {String} category.code  栏目编码
 * @apiVersion 3.3.7
 */

/**
 * @apiDefine returnArticle
 * @apiSuccess {Long} id
 * @apiSuccess {String} title  标题
 * @apiSuccess {String} summary  摘要
 * @apiSuccess {String} keywords  关键词
 * @apiSuccess {String} content  正文
 * @apiSuccess {String} author  作者
 * @apiSuccess {Date} releaseDate  发布日期
 * @apiSuccess {ArticleCategory} category  栏目
 * @apiSuccess {Boolean} issue  发布标示
 * @apiVersion 3.3.7
 */
@RestController
@RequestMapping("/cms/articles")
public class ArticleController {

    @Autowired
    private CmsService cmsService;

    /**
     * @api {get} /cms/articles   查询文章
     * @apiVersion 3.3.7
     * @apiName searchArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 筛选文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/articles?currentPage=1&LIKES_title=上海
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.GET)
    public Pager<Article> search(Pager<Article> pager, List<PropertyFilter> filters) {
        return this.cmsService.findPager(pager, filters);
    }

    /**
     * @param id 文章ID
     * @api {get} /cms/articles/:id   获取文章
     * @apiVersion 3.3.7
     * @apiName getArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 获取单篇文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/articles/43
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
    @RequestMapping("/{id}")
    public Article view(@PathVariable("id") Long id) {
        return this.cmsService.get(id);
    }

    /**
     * @api {post} /cms/articles   添加文章
     * @apiVersion 3.3.7
     * @apiName createArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 添加文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "title=测试&summary=测试&content.text=测试&category.code=root" http://localhost/cms/articles
     * @apiUse paramArticle
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.POST})
    public Article create(Article article) {
        return this.cmsService.save(article);
    }

    /**
     * @api {delete} /cms/articles/:id   删除文章
     * @apiVersion 3.3.7
     * @apiName deleteArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 删除文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/articles/43
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable("id") Long id) {
        this.cmsService.delete(id);
    }

    /**
     * @param id 文章ID
     * @api {delete} /cms/articles   批量删除文章
     * @apiVersion 3.3.7
     * @apiName batchDeleteArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 批量删除文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/articles
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(@RequestBody Long... id) {
        this.cmsService.delete(id);
    }

    /**
     * @api {put} /cms/articles/:id   更新文章
     * @apiVersion 3.3.7
     * @apiName updateArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 更新文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/articles/43
     * @apiUse paramArticle
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT})
    public Article update(@PathVariable("id") Long id, @RequestBody Article article) {
        article.setId(id);
        return this.cmsService.save(article);
    }

}
