package com.fantasy.wx.config.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.bean.WeixinConfig;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IConfigService {
    /**
     * 查找所有配置信息
     *
     * @return
     */
    public List<WeixinConfig> getAll();

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<WeixinConfig> findPager(Pager<WeixinConfig> pager, List<PropertyFilter> filters);

    /**
     * 保存微信配置信息对象
     *
     * @param wc
     * @return 微信配置信息对象
     */
    public WeixinConfig save(WeixinConfig wc);

    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(String... ids);

    /**
     * 通过id查找微信配置对象
     *
     * @param id
     * @return 微信配置对象
     */
    public WeixinConfig get(String id);
}
