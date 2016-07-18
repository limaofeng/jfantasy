package org.jfantasy.cms.rest;

import org.jfantasy.cms.bean.Article;
import org.jfantasy.cms.service.CmsService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "cms-articles", description = "文章接口")
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private CmsService cmsService;

    @ApiOperation(value = "按条件检索文章", notes = "筛选文章，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Article> search(@ApiParam(value = "分页对象", name = "pager") Pager<Article> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.cmsService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取文章", notes = "通过该接口, 获取单篇文章", response = Article.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article view(@PathVariable("id") Long id) {
        return this.cmsService.get(id);
    }

    @ApiOperation(value = "获取文章正文", notes = "通过文章ID, 获取文章正文,由于正文可能比较大，所以单独写了一个接口", response = String.class)
    @RequestMapping(value = "/{id}/content", method = RequestMethod.GET)
    @ResponseBody
    public String viewContent(@PathVariable("id") Long id) {
        return this.cmsService.get(id).getContent().getText();
    }

    @ApiOperation(value = "添加文章", notes = "通过该接口, 添加文章", response = Article.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Article create(@RequestBody Article article) {
        return this.cmsService.save(article);
    }

    @ApiOperation(value = "更新文章", notes = "通过该接口, 更新文章")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Article update(@PathVariable("id") Long id, @RequestBody Article article) {
        article.setId(id);
        return this.cmsService.save(article);
    }

    @ApiOperation(value = "删除文章", notes = "通过该接口, 删除文章")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.cmsService.delete(id);
    }

    @ApiOperation(value = "批量删除文章", notes = "通过该接口, 批量删除文章")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody Long... id) {
        this.cmsService.delete(id);
    }

}
