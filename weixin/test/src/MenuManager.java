import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.util.WeixinUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    @Test
    public void testMessage(){
        Map<String,String> map=new HashMap<String,String>();
        map.put("content","fayayazehong");
        WeixinUtil weixinUtil=new WeixinUtil();
        AccessToken at=new AccessToken();
        at.setAppid("wx0e7cef7ad73417eb");
        at.setAppsecret("e932af31311aeebf76e21a539d9f1944");
        at=weixinUtil.getAccessToken(at);
        //at.setToken("yMpOiOf4IKdWSm0LS7v73g_d7GZ52KxvNSn7YKRLdCVtM9SoCT5Tive_yqJvD4ivGIH61eQY4dr_3Pm-d2Qkpw");
        //weixinUtil.message(at,"o8W9zt_0puksLdwJlqTGXdH9ViRU","text",map);
    }
}
