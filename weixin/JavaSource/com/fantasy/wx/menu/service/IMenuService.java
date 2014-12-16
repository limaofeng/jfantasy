package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.menu.bean.WxMenu;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IMenuService {
    /**
     * 获取所有菜单对象
     *
     * @return 菜单对象集合
     */
    public List<WxMenu> getAll();

    /**
     * 列表查询
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<WxMenu> findPager(Pager<WxMenu> pager, List<PropertyFilter> filters);

    /**
     * 通过条件查找菜单对象集合
     *
     * @param filters
     * @return 菜单集合
     */
    public List<WxMenu> findAll(List<PropertyFilter> filters);

    /**
     * 保存菜单对象集合
     *
     * @param list
     */
    public void saveList(List<WxMenu> list);

    /**
     * 保存菜单对象
     *
     * @param wc
     */
    public WxMenu save(WxMenu wc);

    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(Long... ids);

    /**
     * 通过id获取菜单对象
     *
     * @param id
     * @return菜单对象
     */
    public WxMenu get(Long id);

    /**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int refresh();

    /**
     * 自定义菜单删除接口
     *
     * @return
     */
    public int deleteMenu();
}
