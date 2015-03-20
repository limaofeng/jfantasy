package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.bean.Menu;
import com.fantasy.security.bean.enums.MenuType;
import com.fantasy.security.dao.MenuDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MenuService{

    @Autowired
    private MenuDao menuDao;

    private static final Log logger = LogFactory.getLog(MenuService.class);


    public List<Menu> loadMenus(Long parentId) {
        List<Menu> menus = tree();
        return ObjectUtil.isNull(parentId) ? menus : tree(menus, parentId);
    }

    private List<Menu> tree(List<Menu> menus, Long parentId) {
        for (Menu menu : menus) {
            if (menu.getId().equals(parentId)) {
                return menu.getChildren();
            } else {
                List<Menu> ms = tree(menu.getChildren(), parentId);
                if (ObjectUtil.isNotNull(ms)) {
                    return ms;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parentId:" + parentId + "对于的菜单不存在");
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Menu> tree() {
        return tree(menuDao.getAll());
    }

    @Transactional(readOnly = true)
    public List<Menu> tree(Long menuId) {
        return loadMenus(menuId);
    }

    @Transactional(readOnly = true)
    public List<Menu> tree(List<Menu> menus) {
        Map<Long, List<Menu>> menuMap = new HashMap<Long, List<Menu>>();
        List<Menu> roots = new ArrayList<Menu>();
        for (Menu menu : menus) {
            if (menu.getParent() == null || ObjectUtil.indexOf(menus,"id",menu.getParent().getId()) == -1) {
                roots.add(menu);
            } else {
                if (ObjectUtil.isNull(menuMap.get(menu.getParent().getId()))) {
                    menuMap.put(menu.getParent().getId(), new ArrayList<Menu>());
                }
                menuMap.get(menu.getParent().getId()).add(menu);
            }
        }
        return loadMenus(roots, menuMap);
    }

    private List<Menu> loadMenus(List<Menu> menus, Map<Long, List<Menu>> menuMap) {
        if (ObjectUtil.isNull(menus)){
            return new ArrayList<Menu>();
        }
        for (Menu menu : menus) {
            menu.setChildren(loadMenus(menuMap.get(menu.getId()), menuMap));
            menuMap.remove(menu.getId());
        }
        return ObjectUtil.sort(menus, "sort", "asc");
    }

    public void save(Menu menu) {
        List<Menu> menus;
        boolean root = false;
        if (menu.getParent() == null || StringUtil.isBlank(menu.getParent().getId())) {
            menu.setLayer(0);
            root = true;
            menus = ObjectUtil.sort(menuDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            Menu parentMenu = this.get(menu.getParent().getId());
            menu.setLayer(parentMenu.getLayer() + 1);
            menus = ObjectUtil.sort(menuDao.findBy("parent.id", parentMenu.getId()), "sort", "asc");
        }
        Menu old = menu.getId() != null ? this.menuDao.get(menu.getId()) : null;
        if (old != null) {//更新数据
            if (menu.getSort() != null && (ObjectUtil.find(menus, "id", old.getId()) == null || !old.getSort().equals(menu.getSort()))) {
                if (ObjectUtil.find(menus, "id", old.getId()) == null) {//移动了节点的层级
                    int i = 0;
                    for (Menu m : ObjectUtil.sort((old.getParent() == null || StringUtil.isBlank(old.getParent().getId())) ? menuDao.find(Restrictions.isNull("parent")) : menuDao.findBy("parent.id", old.getParent().getId()), "sort", "asc")) {
                        m.setSort(i++);
                        this.menuDao.save(m);
                    }
                    menus.add(menu.getSort() - 1, menu);
                } else {
                    Menu t = ObjectUtil.remove(menus, "id", old.getId());
                    if (menus.size() >= menu.getSort()) {
                        menus.add(menu.getSort() - 1, t);
                    } else {
                        menus.add(t);
                    }
                }
                //重新排序后更新新的位置
                for (int i = 0; i < menus.size(); i++) {
                    Menu m = menus.get(i);
                    if (m.getId().equals(menu.getId())) {
                        continue;
                    }
                    m.setSort(i + 1);
                    this.menuDao.save(m);
                }
            }
        } else {//新增数据
            menu.setSort(menus.size() + 1);
        }
        if(menu.getPath() == null){
            menu.setPath("");
        }
        this.menuDao.save(menu);
        if (root) {
            menu.setParent(null);
            menu.setPath(menu.getId() + Menu.PATH_SEPARATOR);
        }else{
            menu.setPath(menu.getParent().getPath() + menu.getId() + Menu.PATH_SEPARATOR);
        }
        this.menuDao.update(menu);
    }

    public void delete(Long id) {
        this.menuDao.delete(id);
    }

    public Menu get(Long id) {
        return this.menuDao.get(id);
    }

    public Menu findUniqueByName(String name) {
        return this.menuDao.findUniqueBy("name", name);
    }

    public List<Menu> getAll() {
        return ObjectUtil.sort(menuDao.getAll(), "sort", "asc");
    }

    public Pager<Menu> findPager(Pager<Menu> pager, List<PropertyFilter> filters) {
        return menuDao.findPager(pager, filters);
    }

    public static List<Menu> meunTree(Long value) {
        MenuService menuService = (MenuService) SpringContextUtil.getBean("fantasy.auth.MenuService");
        return menuService.loadMenus(value == 0 ? null : value);
    }

    public static boolean hasSelected(Menu menu, HttpServletRequest request) {
        return hasSelected(menu, request.getRequestURI().replaceAll("^" + request.getContextPath(), ""));
    }

    public static boolean hasSelected(Menu menu, String requestURI) {
        if (MenuType.url.equals(menu.getType()) && requestURI.equals(menu.getValue())) {
            return true;
        } else {
            for (Menu m : menu.getChildren()) {
                if (hasSelected(m, requestURI)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Menu> list(Criterion... criterions) {
        return this.menuDao.find(criterions);
    }

    public List<Menu> list(Criterion[] criterions,String orderBy,String order) {
        return this.menuDao.find(criterions,orderBy,order);
    }


    /**
     * 获取列表
     *
     * @return
     */
    public List<Menu> listRootMenu() {
        return this.menuDao.find(Restrictions.isNull("parent"));
    }

    /**
     * 静态获取列表
     *
     * @return
     */
    public static List<Menu> rootMenuList() {
        MenuService menuService = SpringContextUtil.getBeanByType(MenuService.class);
        return menuService.listRootMenu();
    }

}