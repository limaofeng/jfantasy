package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.bean.WxMenu;
import com.fantasy.wx.dao.WxMenuDao;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信菜单
 */
@Service("wx.MenuService")
@Transactional
public class MenuService {

    @Resource
    private WxMenuDao wxMenuDao;
    @Resource
    private WeixinConfigInit config;

    /**
     * 获取所有菜单对象
     *
     * @return 菜单对象集合
     */
    public List<WxMenu> getAll() {
        return wxMenuDao.getAll();
    }

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<WxMenu> findPager(Pager<WxMenu> pager, List<PropertyFilter> filters) {
        return this.wxMenuDao.findPager(pager, filters);
    }

    /**
     * 通过条件查找菜单对象集合
     *
     * @param filters
     * @return 菜单集合
     */
    public List<WxMenu> findAll(List<PropertyFilter> filters) {
        return this.wxMenuDao.find(filters);
    }

    /**
     * 保存菜单对象
     *
     * @param wc
     */
    public WxMenu save(WxMenu wc) {
        wxMenuDao.save(wc);
        return wc;
    }

    /**
     * 保存菜单对象集合
     *
     * @param list
     */
    public void saveList(List<WxMenu> list) {
        for (WxMenu m : list) {
            wxMenuDao.save(m);
        }
    }

    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.wxMenuDao.delete(id);
        }
    }

    /**
     * 通过id获取菜单对象
     *
     * @param id
     * @return菜单对象
     */
    public WxMenu get(Long id) {
        return this.wxMenuDao.get(id);
    }

    /**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
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

    /**
     * 自定义菜单删除接口
     *
     * @return
     */
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
