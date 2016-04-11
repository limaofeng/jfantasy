package org.jfantasy.security.userdetails;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.security.bean.Menu;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.service.MenuService;
import org.jfantasy.system.util.SettingUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AdminUser extends SimpleUser<User> {

    private static final long serialVersionUID = -9218623309445642304L;

    private List<Menu> menus;

    public AdminUser(User user) {
        super(user);
    }

    public synchronized List<Menu> getMenus() {
        if (menus == null) {
            MenuService menuService = SpringContextUtil.getBeanByType(MenuService.class);
            menus = menuService.tree(menuService.list(new Criterion[]{Restrictions.like("path", SettingUtil.getRootMenuId() + Menu.PATH_SEPARATOR, MatchMode.START), Restrictions.ne("id", SettingUtil.getRootMenuId())}, "sort", "asc"));
        }
        return menus;
    }

}
