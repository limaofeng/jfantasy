package org.jfantasy.cms.rest;

import org.jfantasy.cms.bean.Article;
import org.jfantasy.cms.bean.ArticleCategory;
import org.jfantasy.cms.service.CmsService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "cms-categorys", description = "文章分类接口")
@RestController
@RequestMapping("/categorys")
public class ArticleCategoryController {

    @Autowired
    private CmsService cmsService;

    @ApiOperation(value = "查询文章分类", notes = "通过该接口, 筛选文章分类")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ArticleCategory> search() {
        return cmsService.getCategorys();
    }


    @ApiOperation(value = "获取文章分类", notes = "通过该接口, 获取文章分类", response = ArticleCategory.class)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    @ResponseBody
    public ArticleCategory view(@PathVariable("code") String code) {
        return this.cmsService.get(code);
    }


    @ApiOperation(value = "添加文章分类", notes = "通过该接口, 添加文章分类", response = ArticleCategory.class)
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ArticleCategory create(@RequestBody ArticleCategory category) {
        return this.cmsService.save(category);
    }

    @ApiOperation(value = "删除文章分类", notes = "通过该接口, 删除文章分类")
    @RequestMapping(value = "/{code}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable("code") String code) {
        this.cmsService.delete(code);
    }

    @ApiOperation(value = "批量删除分类", notes = "通过该接口, 批量删除分类")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... code) {
        this.cmsService.delete(code);
    }

    @ApiOperation(value = "更新文章分类", notes = "通过该接口, 更新文章分类", response = ArticleCategory.class)
    @RequestMapping(value = "/{code}", method = RequestMethod.PATCH)
    @ResponseBody
    public ArticleCategory update(@PathVariable("code") String code, @RequestBody ArticleCategory category) {
        category.setCode(code);
        return this.cmsService.save(category);
    }

    @ApiOperation(value = "查询文章分类下的文章", notes = "通过该接口, 获取分类下的文章")
    @RequestMapping(value = "/{code}/articles", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Article> articles(@PathVariable("code") String code, Pager<Article> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_category.code", code));
        return this.cmsService.findPager(pager, filters);
    }

}
