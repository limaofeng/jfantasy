package com.fantasy.wx.config.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.bean.WeixinConfig;
import com.fantasy.wx.config.dao.WeixinConfigDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zzzhong on 2014/9/18.
 */
@Service
@Transactional
public class WeixinConfigService {

    @Resource
    private WeixinConfigDao weixinConfigDao;

    public List<WeixinConfig> getAll(){
        return weixinConfigDao.getAll();
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
    public Pager<WeixinConfig> findPager(Pager<WeixinConfig> pager, List<PropertyFilter> filters) {
        return this.weixinConfigDao.findPager(pager, filters);
    }
    public WeixinConfig save(WeixinConfig wc){
        weixinConfigDao.save(wc);
        return wc;
    }
    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(String... ids) {
        for (String id : ids) {
            WeixinConfig m=this.weixinConfigDao.get(id);
            this.weixinConfigDao.delete(m);
        }
    }
    public WeixinConfig get(String id) {
        return this.weixinConfigDao.get(id);
    }
}
