package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.dao.AccessTokenDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzzhong on 2014/9/18.
 */
@Service
@Transactional
public class AccessTokenService {

    @Resource
    private AccessTokenDao accessTokenDao;

    public List<AccessToken> getAll(){
        return accessTokenDao.getAll();
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
    public Pager<AccessToken> findPager(Pager<AccessToken> pager, List<PropertyFilter> filters) {
        return this.accessTokenDao.findPager(pager, filters);
    }
    public AccessToken save(AccessToken at){
        accessTokenDao.save(at);
        return at;
    }
    /**
     * 根据id 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        for (Long id : ids) {
            AccessToken m=this.accessTokenDao.get(id);
            this.accessTokenDao.delete(m);
        }
    }
    public AccessToken get(Long id) {
        return this.accessTokenDao.get(id);
    }
}
