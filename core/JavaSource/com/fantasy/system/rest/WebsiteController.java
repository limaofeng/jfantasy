package com.fantasy.system.rest;

import com.fantasy.security.bean.Menu;
import com.fantasy.security.service.MenuService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "system-websites", description = "站点信息")
@RestController
@RequestMapping("/system/websites")
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Website> websites() {
        return this.websiteService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Website create(@RequestBody Website website) {
        return this.websiteService.save(website);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    @ResponseBody
    public Website create(@PathVariable("key") String key, @RequestBody Website website) {
        website.setKey(key);
        return this.websiteService.save(website);
    }

    @ApiOperation(value = "获取网站对应的菜单信息", notes = "通过该接口, 可以获取网站配置的菜单信息。")
    @RequestMapping(value = "/{key}/menus", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> menus(@PathVariable("key") String key) {
        Website website = this.websiteService.findUniqueByKey(key);
        Long rootMenuId = website.getRootMenu().getId();
        return menuService.list(new Criterion[]{Restrictions.like("path", rootMenuId + Menu.PATH_SEPARATOR, MatchMode.START), Restrictions.ne("id", rootMenuId)}, "sort", "asc");
    }

}
