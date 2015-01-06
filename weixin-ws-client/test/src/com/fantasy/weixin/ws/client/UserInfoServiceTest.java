package com.fantasy.weixin.ws.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fantasy.uitl.Constants;
import org.junit.Before;

import javax.annotation.Resource;

/**
 * Created by zzzhong on 2015/1/6.
 */
public class UserInfoServiceTest {
    private final static Log LOG = LogFactory.getLog(UserInfoServiceTest.class);
    @Resource
    private UserInfoService userInfoService;
    @Before
    public void init() throws Exception {
        userInfoService = new UserInfoService();
        userInfoService.setEndPointReference(Constants.END_POINT_REFERENCE);
        userInfoService.setTargetNamespace("http://ws.goods.mall.fantasy.com");
        userInfoService.afterPropertiesSet();
    }
}
