package com.fantasy.wx.factory;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.wx.WeiXinCoreHelper;
import com.fantasy.wx.account.AccountDetailsService;
import com.fantasy.wx.session.AccountDetails;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


public class WeiXinSessionFactoryBean implements FactoryBean<WeiXinSessionFactory>, InitializingBean {

    /**
     * 微信session工厂
     */
    private WeiXinSessionFactory weiXinSessionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultWeiXinSessionFactory factory = new DefaultWeiXinSessionFactory();
        factory.setAccountDetailsService(SpringContextUtil.getBeanByType(AccountDetailsService.class));
        factory.setWeiXinCoreHelper(SpringContextUtil.getBeanByType(WeiXinCoreHelper.class));

        this.weiXinSessionFactory = factory;

        for (AccountDetails accountDetails : weiXinSessionFactory.getAccountDetailsService().getAll()) {
            weiXinSessionFactory.getWeiXinCoreHelper().register(accountDetails);
        }

    }

    @Override
    public WeiXinSessionFactory getObject() throws Exception {
        return weiXinSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return weiXinSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
