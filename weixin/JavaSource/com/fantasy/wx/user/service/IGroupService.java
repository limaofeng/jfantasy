package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.bean.WxGroup;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IGroupService {
    /**
     * 保存分组对象
     *
     * @param wxGroup
     * @return
     */
    public WxGroup save(WxGroup wxGroup);

    /**
     * 查询所有分组对象
     *
     * @return
     */
    public List<WxGroup> getAll();

    /**
     * 查询分组的分页对象
     *
     * @param pager
     * @param list
     * @return 分页对象
     */
    public Pager<WxGroup> findPager(Pager<WxGroup> pager, List<PropertyFilter> list);

    /**
     * 获取分组对象
     *
     * @param id
     * @return
     */
    public WxGroup getGroup(Long id);

    /**
     * 删除分组对象
     *
     * @param ids
     */
    public void delete(Long... ids);

    /**
     * 创建分组
     *
     * @param name
     * @return
     */
    public int create(String name);

    /**
     * 更新分组名称
     *
     * @param id
     * @param name
     * @return
     */
    public int update(Long id, String name);

    /**
     * 刷新用户分组
     *
     * @return
     */
    public List<WxGroup> refreshGroup();

    /**
     * 获取用户所在分组的groupId
     *
     * @param openId 用户id
     * @return groupid
     * @throws
     */
    public Long getUserGroup(String openId) throws WeiXinException;

    /**
     * 移动分组
     *
     * @param openId  用户id
     * @param groupId 分组id
     * @return
     */
    public int moveGroup(String openId, Long groupId);
}
