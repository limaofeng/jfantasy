package org.jfantasy.security.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.security.bean.Menu;
import org.jfantasy.security.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单
 */
@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private transient MenuService menuService;

    /**
     * 查询菜单<br/>
     * 筛选文章，返回菜单数组
     * @param pager
     * @param filters
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> search(Pager<Menu> pager, List<PropertyFilter> filters) {
        if (!pager.isOrderBySetted()) {
            pager.setOrder(Pager.SORT_ASC);
            pager.setOrderBy("sort");
        }
        return this.menuService.findPager(pager, filters).getPageItems();
    }

    /**
     * 获取菜单
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Menu view(@PathVariable("id") String id) {
        return this.menuService.get(id);
    }

    /**
     * 删除菜单
     * @param id
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        this.menuService.delete(id);
    }

    /**
     * 批量删除菜单
     * @param ids
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... ids) {
        this.menuService.delete(ids);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Menu create(@RequestBody Menu menu) {
        return menuService.save(menu);
    }

    /**
     * 更新菜单
     * @param id
     * @param menu
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    @ResponseBody
    public Menu update(@PathVariable("id") String id, @RequestBody Menu menu) {
        menu.setId(id);
        return menuService.save(menu);
    }

}
