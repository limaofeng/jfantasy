package com.fantasy.weixin.ws.client;

import com.fantasy.uitl.Constants;
import com.fantasy.weixin.ws.dto.UserInfoDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by zzzhong on 2015/1/6.
 */
public class UserInfoServiceTest {
    private final static Log LOG = LogFactory.getLog(UserInfoServiceTest.class);
    @Autowired
    private UserInfoService userInfoService;
    @Before
    public void init() throws Exception {
        userInfoService = new UserInfoService();
        userInfoService.setEndPointReference(Constants.END_POINT_REFERENCE);
        userInfoService.setTargetNamespace("http://ws.weixin.fantasy.com");
        userInfoService.afterPropertiesSet();
    }
    @Test
    public void testRefresh(){
        UserInfoDTO u= userInfoService.getUserInfo("o8W9zt_0puksLdwJlqTGXdH9ViRU");
        Assert.notNull(u);
        LOG.debug(u);
    }
}
