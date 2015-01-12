package com.fantasy.framework.tags.ui;

import com.fantasy.framework.tags.AbstractUITag;
import com.fantasy.framework.tags.model.Navigation;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.Menu;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;

public class NavigationTag extends AbstractUITag<Navigation> {
    private static final long serialVersionUID = 5686662675123002265L;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int doStartTag() throws JspException {
	List<Menu> leftMenus = (List<Menu>) ObjectUtil.defaultValue(getRequest().getSession().getAttribute("leftMenus"), new ArrayList());
	List<Menu> menus = new ArrayList<Menu>();
	if (leftMenus.size() > 0){
        menus.add(((Menu) leftMenus.get(0)).getParent());
    }
	loadMenus(menus, leftMenus);
	((Navigation) getUIBean()).setMenus(menus);
	return super.doStartTag();
    }

    private void loadMenus(List<Menu> menus, List<Menu> leftMenus) {
	if (leftMenus == null){
        return;
    }
    }
}