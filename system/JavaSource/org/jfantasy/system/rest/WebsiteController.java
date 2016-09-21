package org.jfantasy.system.rest;

import org.jfantasy.system.bean.Website;
import org.jfantasy.system.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 站点信息 **/
@RestController
@RequestMapping("/websites")
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Website> websites() {
        return this.websiteService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Website create(@RequestBody Website website) {
        return this.websiteService.save(website);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PATCH)
    @ResponseBody
    public Website create(@PathVariable("key") String key, @RequestBody Website website) {
        website.setKey(key);
        return this.websiteService.save(website);
    }

    /*
    @ApiOperation(value = "获取网站对应的菜单信息", notes = "通过该接口, 可以获取网站配置的菜单信息。")
    @RequestMapping(value = "/{key}/menus", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> menus(@PathVariable("key") String key) {
        Website website = this.websiteService.findUniqueByKey(key);
        Long rootMenuId = website.getRootMenuId();
        return menuService.list(new Criterion[]{Restrictions.like("path", rootMenuId + Menu.PATH_SEPARATOR, MatchMode.START), Restrictions.ne("id", rootMenuId)}, "sort", "asc");
    }*/

}
