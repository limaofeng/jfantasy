package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.menu.bean.Menu;
import com.fantasy.wx.menu.dao.MenuDao;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
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
public class MenuService {

    @Resource
    private MenuDao menuDao;
    @Resource
    private WeixinConfigInit config;

    public List<Menu> getAll(){
        return menuDao.getAll();
    }
    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<Menu> findPager(Pager<Menu> pager, List<PropertyFilter> filters) {
        return this.menuDao.findPager(pager, filters);
    }
    public List<Menu> findAll(List<PropertyFilter> filters){
        return this.menuDao.find(filters);
    }
    public Menu save(Menu wc){
        menuDao.save(wc);
        return wc;
    }

    public void saveList(List<Menu> list){
        for(Menu m:list){
            menuDao.save(m);
        }
    }
    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            Menu m=this.menuDao.get(id);
            this.menuDao.delete(m);
        }
    }
    public Menu get(Long id) {
        return this.menuDao.get(id);
    }

    public int refresh(){
        WxMenu menu = new WxMenu();
        List<PropertyFilter> list=new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer","0"));
        List<Menu> menus=menuDao.find(list);
        for(int i=0;i<menus.size();i++){
            Menu m=menus.get(i);
            WxMenu.WxMenuButton menuButton=createBtn(m);
            if(m.getChildren()!=null&&m.getChildren().size()>0){
                menus=m.getChildren();
                for(int j=0;j<menus.size();j++){
                    menuButton.getSubButtons().add(createBtn(menus.get(j)));
                }
            }
            menu.getButtons().add(menuButton);
        }
        try {
            config.getUtil().menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        menuDao.batchSQLExecute("update wx_menu set off=1");
        return 0;
    }
    public int deleteMenu(){
        try {
            config.getUtil().menuDelete();
            menuDao.batchSQLExecute("delete wx_menu");
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
        return 0;
    }
    public WxMenu.WxMenuButton createBtn(Menu m){
        WxMenu.WxMenuButton menuButton=new WxMenu.WxMenuButton();
        menuButton.setName(m.getName());
        menuButton.setKey(m.getKey());
        menuButton.setUrl(m.getUrl());
        menuButton.setType(m.getType());
        return menuButton;
    }
}
