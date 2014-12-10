package com.fantasy.wx.menu.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.menu.bean.WxMenu;
import com.fantasy.wx.menu.dao.WxMenuDao;
import com.fantasy.wx.menu.service.IMenuService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/9/18.
 */
@Service
@Transactional
public class WxMenuService implements IMenuService {

    @Resource
    private WxMenuDao wxMenuDao;
    @Resource
    private WeixinConfigInit config;

    @Override
    public List<WxMenu> getAll() {
        return wxMenuDao.getAll();
    }

    @Override
    public Pager<WxMenu> findPager(Pager<WxMenu> pager, List<PropertyFilter> filters) {
        return this.wxMenuDao.findPager(pager, filters);
    }

    @Override
    public List<WxMenu> findAll(List<PropertyFilter> filters) {
        return this.wxMenuDao.find(filters);
    }

    @Override
    public WxMenu save(WxMenu wc) {
        wxMenuDao.save(wc);
        return wc;
    }

    @Override
    public void saveList(List<WxMenu> list) {
        for (WxMenu m : list) {
            wxMenuDao.save(m);
        }
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.wxMenuDao.delete(id);
        }
    }

    @Override
    public WxMenu get(Long id) {
        return this.wxMenuDao.get(id);
    }

    @Override
    public int refresh() {
        me.chanjar.weixin.common.bean.WxMenu wxMenu = new me.chanjar.weixin.common.bean.WxMenu();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer", "0"));
        Pager<WxMenu> pager = new Pager<WxMenu>();
        pager.setOrderBy("sort");
        pager.setOrder(Pager.Order.asc);
        Pager<WxMenu> p = wxMenuDao.findPager(pager, list);
        for (int i = 0; i < (p.getPageItems().size() > 3 ? 3 : p.getPageItems().size()); i++) {
            WxMenu m = p.getPageItems().get(i);
            me.chanjar.weixin.common.bean.WxMenu.WxMenuButton menuButton = createBtn(m);
            if (m.getChildren() != null && m.getChildren().size() > 0) {
                List<WxMenu> wxMenus = m.getChildren();
                for (int j = 0; j < (wxMenus.size() > 5 ? 5 : wxMenus.size()); j++) {
                    menuButton.getSubButtons().add(createBtn(wxMenus.get(j)));
                }
            }
            wxMenu.getButtons().add(menuButton);
        }
        try {
            config.getUtil().menuCreate(wxMenu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    @Override
    public int deleteMenu() {
        try {
            config.getUtil().menuDelete();
            wxMenuDao.batchSQLExecute("delete from wx_menu WHERE layer != 0");
            wxMenuDao.batchSQLExecute("delete from wx_menu");
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }

    public me.chanjar.weixin.common.bean.WxMenu.WxMenuButton createBtn(WxMenu m) {
        me.chanjar.weixin.common.bean.WxMenu.WxMenuButton menuButton = new me.chanjar.weixin.common.bean.WxMenu.WxMenuButton();
        menuButton.setName(m.getName());
        menuButton.setKey(m.getKey());
        menuButton.setUrl(m.getUrl());
        menuButton.setType(m.getType());
        return menuButton;
    }
}
