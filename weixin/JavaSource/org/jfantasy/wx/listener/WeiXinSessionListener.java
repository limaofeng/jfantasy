package org.jfantasy.wx.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WeiXinSessionListener implements ApplicationListener<AuthenticationSuccessEvent> {

    public static final String WEIXIN_APPID = "WEIXIN_APPID";

//    @Autowired
//    private AccountMappingService accountMappingService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails details = (UserDetails) event.getAuthentication().getPrincipal();
        if (details != null) {
            /*
            Map<String, Object> data = ClassUtil.getValue(details, "data");
            if (data != null && !data.containsKey(WEIXIN_APPID)) {
                Account account = accountMappingService.getAccount("user", details.getUsername());
                if (account != null) {
                    data.put(WEIXIN_APPID, account.getAppId());
                }
            }*/
        }
    }
}
