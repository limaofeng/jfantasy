package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.account.Consts;
import com.fantasy.wx.bean.MenuWeixin;
import com.fantasy.wx.dao.MenuDao;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.message.content.Menu;
import com.fantasy.wx.framework.oauth2.Scope;
import com.fantasy.wx.framework.session.WeiXinSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信菜单
 */
@Service
@Transactional
public class MenuWeiXinService  implements InitializingBean {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private WeiXinSessionFactory factory;

    private WeiXinSession session;

    /**
     * 获取所有菜单对象
     *
     * @return 菜单对象集合
     */
    public List<MenuWeixin> getAll() {
        return menuDao.getAll();
    }

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<MenuWeixin> findPager(Pager<MenuWeixin> pager, List<PropertyFilter> filters) {
        return this.menuDao.findPager(pager, filters);
    }

    /**
     * 通过条件查找菜单对象集合
     *
     * @param filters
     * @return 菜单集合
     */
    public List<MenuWeixin> findAll(List<PropertyFilter> filters) {
        return this.menuDao.find(filters);
    }

    /**
     * 保存菜单对象
     *
     * @param wc
     */
    public MenuWeixin save(MenuWeixin wc) {
        menuDao.save(wc);
        return wc;
    }

    /**
     * 保存菜单对象集合
     *
     * @param list
     */
    public void saveList(List<MenuWeixin> list) {
        for (MenuWeixin m : list) {
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
            this.menuDao.delete(id);
        }
    }

    /**
     * 通过id获取菜单对象
     *
     * @param id
     * @return菜单对象
     */
    public MenuWeixin get(Long id) {
        return this.menuDao.get(id);
    }

    /**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int refresh() {
        Menu[] mArray=new Menu[3];
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer", "0"));
        Pager<MenuWeixin> pager = new Pager<MenuWeixin>();
        pager.setOrderBy("sort");
        pager.setOrder(Pager.Order.asc);
        Pager<MenuWeixin> p = menuDao.findPager(pager, list);
        for (int i = 0; i < (p.getPageItems().size() > 3 ? 3 : p.getPageItems().size()); i++) {
            MenuWeixin m = p.getPageItems().get(i);
            Menu.MenuType type = StringUtil.isBlank(m.getType()) ? Menu.MenuType.UNKNOWN : Menu.MenuType.valueOf(m.getType().toUpperCase());
            if (m.getChildren().isEmpty()) {
                mArray[i]=new Menu(type, m.getName(), StringUtil.defaultValue(m.getKey(), m.getUrl()));
            } else {
                List<Menu> subMenus = new ArrayList<Menu>();
                for (MenuWeixin menus: m.getChildren()) {
                    subMenus.add(new Menu(Menu.MenuType.valueOf(menus.getType().toUpperCase()), menus.getName(), StringUtil.defaultValue(menus.getKey(), menus.getUrl())));
                }
                mArray[i]=new Menu(type, m.getName(), StringUtil.defaultValue(m.getKey(), m.getUrl()), subMenus.toArray(new Menu[subMenus.size()]));
            }
        }
        session.refreshMenu(mArray);
        return 0;
    }

    /**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int refresh(Menu... menu) {
        //menuWeixinList.add(new MenuWeixin("商场首页", WxConsts.BUTTON_VIEW, 1, 1, "http://112.124.22.92:8080/iziwx/bazaar/index",null, m1));
        Menu m=null;
        for(int i=0;i<menu.length;i++){
            m=menu[i];
            MenuWeixin mwx=new MenuWeixin(m.getName(),m.getType().toString(),0,i,m.getUrl(),m.getKey(),null);
            menuDao.save(mwx);
            if(!m.getChildren().isEmpty()){
                Menu subm;
                for(int j=0,si=m.getChildren().size();j<si;j++){
                    subm=m.getChildren().get(j);
                    menuDao.save(new MenuWeixin(subm.getName(),subm.getType().toString(),1,j+1,subm.getUrl(),subm.getKey(),mwx));
                }
            }
        }
        session.refreshMenu(menu);
        return 0;
    }

    public String createOauth2Url(String redirectUri, Scope scope, String state){
        return session.getAuthorizationUrl(redirectUri,scope,state);
    }
    public String createOauth2Url(String redirectUri, Scope scope){
        return session.getAuthorizationUrl(redirectUri,scope);
    }

    /**
     * 自定义菜单删除接口
     *
     * @return
     */
    public int deleteMenu() {
        session.clearMenu();
        menuDao.batchSQLExecute("delete from wx_menu WHERE layer != 0");
        menuDao.batchSQLExecute("delete from wx_menu");
        return 0;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        session = Consts.appid == null ? null : factory.openSession(Consts.appid);
    }
}
