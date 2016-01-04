package org.jfantasy.cms.rest;

import org.jfantasy.cms.bean.Banner;
import org.jfantasy.cms.service.BannerService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "cms-banners", description = "轮播图接口")
@RestController
@RequestMapping("/cms/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "添加轮播图", notes = "添加轮播图", response = Banner.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Banner create(@RequestBody Banner banner) {
        return bannerService.save(banner);
    }

    @ApiOperation(value = "更新轮播图", notes = "更新轮播图信息", response = Banner.class)
    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    @ResponseBody
    public Banner update(@PathVariable("key") String key,@RequestBody Banner banner) {
        banner.setKey(key);
        return bannerService.save(banner);
    }

    @ApiOperation(value = "获取轮播图", notes = "获取轮播图", response = Banner.class)
    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Banner view(@PathVariable("key") String key) {
        return bannerService.get(key);
    }

    @ApiOperation(value = "删除轮播图", notes = "删除轮播图")
    @RequestMapping(value = "/{key}", method = {RequestMethod.DELETE})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("key") String key) {
        bannerService.delete(key);
    }

    @ApiOperation(value = "批量删除轮播图", notes = "批量删除轮播图")
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... key) {
        bannerService.delete(key);
    }

    @ApiOperation(value = "查询轮播图", notes = "查询轮播图")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Banner> search(@ApiParam(value = "分页对象", name = "pager") Pager<Banner> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.bannerService.findPager(pager, filters);
    }

}

