package org.jfantasy.security.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.security.bean.Menu;
import org.jfantasy.security.dao.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    private static final Log LOGGER = LogFactory.getLog(MenuService.class);

    public Menu save(Menu menu) {
        List<Menu> menus;
        boolean root = false;
        if (menu.getParent() == null || StringUtil.isBlank(menu.getParent().getId())) {
            menu.setLayer(1);
            root = true;
            menus = ObjectUtil.sort(menuDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            Menu parentMenu = this.get(menu.getParent().getId());
            menu.setLayer(parentMenu.getLayer() + 1);
            menu.setParent(parentMenu);
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
        if (menu.getPath() == null) {
            menu.setPath("");
        }
        if (old != null) {
            menu = this.menuDao.merge(menu);
        } else {
            menu = this.menuDao.save(menu);
        }
        if (root) {
            menu.setParent(null);
            menu.setPath(Menu.PATH_SEPARATOR + menu.getId());
        } else {
            menu.setPath(menu.getParent().getPath() + Menu.PATH_SEPARATOR + menu.getId());
        }
        this.menuDao.update(menu);
        return menu;
    }

    public void delete(String... ids) {
        for (String id : ids) {
            this.menuDao.delete(id);
        }
    }

    public Menu get(String id) {
        return this.menuDao.get(id);
    }

    public Pager<Menu> findPager(Pager<Menu> pager, List<PropertyFilter> filters) {
        return menuDao.findPager(pager, filters);
    }

    public List<Menu> list(Criterion... criterions) {
        return this.menuDao.find(criterions);
    }

    public List<Menu> list(Criterion[] criterions, String orderBy, String order) {
        return this.menuDao.find(criterions, orderBy, order);
    }

}