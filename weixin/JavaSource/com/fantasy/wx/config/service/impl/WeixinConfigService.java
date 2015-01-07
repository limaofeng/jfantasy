package com.fantasy.wx.config.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.bean.WeixinConfig;
import com.fantasy.wx.config.dao.WeixinConfigDao;
import com.fantasy.wx.config.service.IConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zzzhong on 2014/9/18.
 */
@Service("wxWeixinConfigService")
@Transactional
public class WeixinConfigService implements IConfigService {

    @Resource
    private WeixinConfigDao weixinConfigDao;

    @Override
    public List<WeixinConfig> getAll() {
        return weixinConfigDao.getAll();
    }

    @Override
    public Pager<WeixinConfig> findPager(Pager<WeixinConfig> pager, List<PropertyFilter> filters) {
        return this.weixinConfigDao.findPager(pager, filters);
    }

    @Override
    public WeixinConfig save(WeixinConfig wc) {
        weixinConfigDao.save(wc);
        return wc;
    }

    @Override
    public void delete(String... ids) {
        for (String id : ids) {
            WeixinConfig m = this.weixinConfigDao.get(id);
            this.weixinConfigDao.delete(m);
        }
    }

    @Override
    public WeixinConfig get(String id) {
        return this.weixinConfigDao.get(id);
    }
}
