import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.bean.pojo.group.GroupMessage;
import com.fantasy.wx.bean.pojo.group.TextGroupMessage;
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
    @Test
    public void sendGroupMessage(){
        WeixinUtil weixinUtil=new WeixinUtil();
        AccessToken at=new AccessToken();
        at.setToken("U3Byp1Yz-B9_YBub_nKvu5oKFrN9Oj5ArP7bmTTqGkaamGWesiml-zCDZ2e5521Q7YZRrLNFQUz3TBiOqAbfXdYm2Si4nQMUYkWBv1X5IH8");
        TextGroupMessage groupMessage=new TextGroupMessage();
        groupMessage.setTouser(new String[]{"oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw"});
        TextGroupMessage.Text text=groupMessage.new Text();
        text.setContent("我来发群发消息啦！接到的有糖果吃");
        groupMessage.setMsgtype("text");
        groupMessage.setText(text);
        weixinUtil.createGroupMessage(groupMessage,at.getToken());
    }
}
