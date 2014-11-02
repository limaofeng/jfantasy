package com.fantasy.wx.util;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.bean.pojo.Menu;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.pojo.WatchUserList;
import com.fantasy.wx.bean.pojo.code.QCode;
import com.fantasy.wx.bean.pojo.group.GroupMessage;
import com.fantasy.wx.bean.pojo.group.Material;
import com.fantasy.wx.bean.resp.ArticleMessage;
import com.fantasy.wx.bean.resp.Code;
import com.fantasy.wx.bean.resp.NewsMessage;
import com.fantasy.wx.bean.resp.TextMessage;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 公众平台通用接口工具类
 * Created by zzzhong on 2014/6/18.
 */
public class WeixinUtil {
    /**
     * 系统中配置的token配置
     * string = appi
     * accessToken = token对象
     */
    public static Map<String,AccessToken> accessToken = new HashMap<String, AccessToken>();
    private static AccessToken firstAccessToken;
    public final static ObjectMapper objectMapper=new ObjectMapper();
    static{
        // 当找不到对应的序列化器时 忽略此字段
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 允许非空字段
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 失败在未知属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    // 菜单创建（POST） 限100（次/天）
    public  String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 获取access_token的接口地址（GET） 限200（次/天）
    public String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //获取用户基本信息的接口地址（GET）
    public String user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //通过code换取网页授权access_token
    private String web_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    //群发消息地址（GET） 每日调用限制为100次 用户每月只能接收4条
    public String message_create_url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
    //上传图片的地址
    public String uploadImage="https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
    //关注用户列表
    public String watch_user_list_url="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
    //生成二维码
    public String qrcode="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    //发送客服消息
    public String messageUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    /**
     * 获取access_token
     *
     * @return
     */
    public AccessToken getAccessToken(AccessToken at) {
        String requestUrl = access_token_url.replace("APPID", at.getAppid()).replace("APPSECRET", at.getAppsecret());
        String json = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != json) {
            Map<String,Object> map= (Map<String, Object>) JSON.deserialize(json);
            at.setToken(map.get("access_token").toString());
            at.setExpiresIn((Integer)map.get("expires_in"));
            System.out.println("appid:"+at.getAppid()+"获取tokenName:"+at.getTokenName()+"成功，有效时长"+at.getExpiresIn()+"秒 token:"+at.getToken());
        }else{
            System.out.println("获取token失败！");
        }
        return at;
    }
    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            String json=buffer.toString();
            Map<String,Object> map= (Map<String, Object>) JSON.deserialize(json);
            if(map.containsKey("errcode")){
                System.out.println("errcode:"+map.get("errcode")+" errmsg:"+map.get("errmsg"));
                if((Integer)map.get("errcode")==0){
                    return "0";
                }
            }else{
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建二维码
     * @param qcode
     * @param at
     * @return
     */
    public Code createCode(QCode qcode,AccessToken at){
        Code code=null;
        String requestUrl = qrcode.replace("ACCESS_TOKEN", at.getToken());
        // 将菜单对象转换成json字符串
        String jsonCode = JSON.serialize(qcode);
        // 调用接口创建菜单
        String json = httpRequest(requestUrl, "POST", jsonCode);
        if (null != json) {
            code=JSON.deserialize(json, Code.class);
        }else{
            System.out.println("创建二维码失败!");
        }
        return code;
    }
    /**
     * 获取关注者列表
     * @param at
     * @return
     */
    public WatchUserList getWatchUserList(AccessToken at){
        String requestUrl = watch_user_list_url.replace("ACCESS_TOKEN", at.getToken());
        WatchUserList watchUserList=null;
        String json = httpRequest(requestUrl, "GET", null);
        if(json!=null){
            watchUserList=JSON.deserialize(json, WatchUserList.class);
        } else{
            System.out.println("获取WatchUserList失败");
        }
        return watchUserList;
    }
    /**
     * 获取用户基本信息
     *
     * @param openId 用户开放id
     * @return
     */
    public UserInfo getUserInfo(String openId,AccessToken at){
        UserInfo ui= null;
        String requestUrl = user_info_url.replace("ACCESS_TOKEN", at.getToken()).replace("OPENID", openId);
        System.out.println("requestUrl"+requestUrl);
        String json = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null == json) {
            System.out.println("获取userInfo失败");
        }else{
            ui=JSON.deserialize(json, UserInfo.class);
        }
        return ui;
    }
    /**
     * 获取OpenId
     * 网页授权code
     * @return
     */
    public String getOpenId(String code,AccessToken at) {
        String requestUrl = web_token_url.replace("APPID", at.getAppid()).replace("APPSECRET", at.getAppsecret()).replace("CODE", code);
        System.out.println("getOpenId"+requestUrl);
        String json = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null == json) {
            System.out.println("获取openId失败");
        }else{
            Map<String,String> map= (Map<String, String>) JSON.deserialize(json);
            return map.get("openid");
        }
        return "";
    }
    /*
     * 创建图文消息
     * @param fromUserName
     * @param toUserName
     * @param articleList 图文消息集合
     * @return
     */
    public String newMessage(String fromUserName,String toUserName,List<ArticleMessage> articleList){
        NewsMessage newsMessage = new NewsMessage();
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setFuncFlag(0);
        // 设置图文消息个数
        newsMessage.setArticleCount(articleList.size());
        // 设置图文消息包含的图文集合
        newsMessage.setArticles(articleList);
        // 将图文消息对象转换成xml字符串
        return MessageUtil.newsMessageToXml(newsMessage);
    }
    /*
     * 文本消息
     * @param fromUserName
     * @param toUserName
     * @param content
     * @return
     */
    public String textMessage(String fromUserName,String toUserName,String content){
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        textMessage.setContent(content);
        return MessageUtil.textMessageToXml(textMessage);
    }
    /*
     * 创建菜单
     *
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，1表示失败
     */
    public int createMenu(Menu menu, String accessToken) {
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSON.text().serialize(menu);
        // 调用接口创建菜单
        String json = httpRequest(url, "POST", jsonMenu);

        if (null == json) {
            System.out.println("创建菜单失败");
            return 1;
        }
        return 0;
    }
    public int deleteMenu(String accessToken){
        String url="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口创建菜单
        String json = httpRequest(url, "POST", "");
        if (null == json) {
            System.out.println("删除菜单失败");
            return 1;
        }
        return 0;
    }
    /*
     * 创建群发消息
     *
     * @param gm 群发实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，1表示失败
     */
    public int createGroupMessage(GroupMessage gm, String accessToken) {
        // 拼装群发信息的url
        String url = message_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将群发信息对象转换成json字符串
        String gmjson =JSON.text().serialize(gm);
        // 调用群发接口
        String json = httpRequest(url, "GET", gmjson);
        if (null == json) {
            System.out.println("创建群发消息失败");
            return 1;
        }
        return 0;
    }
    /*
     * 上传图文消息素材
     *
     * @param accessToken 有效的access_token
     */
    public String uploadImages( String accessToken) {
        // 拼装群发信息的url
        String url = uploadImage.replace("ACCESS_TOKEN", accessToken);
        Material material=new Material();
        List<Material.Articles> articlesList=new ArrayList<Material.Articles>();
        Material.Articles a=material.new Articles();
        a.setAuthor("xxx");
        a.setTitle("Happy Day");
        a.setContent("content");
        a.setThumb_media_id("o4eO6d0Bn5dfXS_UOVCsbdPaXARGf9eKk0kG3rrNDpEMF329HAwoVecqzU3dr5aq");
        a.setContent_source_url("www.qq.com");
        a.setDigest("digest");
        a.setShow_cover_pic("0");
        articlesList.add(a);
        material.setArticles(articlesList);
        String materialJson = JSON.serialize(material);
        String json = httpRequest(url, "POST", materialJson);
        if(json==null){
            Map<String,String> map= (Map<String, String>) JSON.deserialize(json);
            return map.get("media_id");
        }else{
            System.out.println("发送群发消息失败");
        }
        return null;
    }
    /*
     * 文件上传到微信服务器
     * @param fileType 文件类型
     * @param filePath 文件路径
     * @return JSONObject
     * @throws Exception
     */
    public String sendFile(String fileType, String filePath,String accessToken) throws Exception {
        String result = null;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }
        /*
         * 第一部分
         */
        URL urlObj = new URL("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+ accessToken + "&type="+fileType+"");
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                buffer.append(line);
            }
            if(result==null){
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if(reader!=null){
                reader.close();
            }
        }
        return result;
    }
    public void init(){

    }

    /**
     * 创建客服回复消息
     * @param at
     * @param openId
     * @param type text,image...
     * @param param 微信官网各种类型的信息回复格式
     */
    public boolean message(AccessToken at,String openId,String type,Map<?,?> param){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("touser",openId);
        map.put("msgtype",type);
        map.put(type,param);
        String requestUrl = messageUrl.replace("ACCESS_TOKEN", at.getToken());
        String jsonCode = JSON.text().serialize(map);
        String result=httpRequest(requestUrl, "POST", jsonCode);
        return result!=null;
    }
    public static <T> T toBean(Object obj, Class<T> newClass) {
        if (obj == null)
            return null;
        return (T) JSON.deserialize(JSON.serialize(obj), newClass);
    }
    public static AccessToken firstAccessToken(){
        if(firstAccessToken!=null) return firstAccessToken;
        if(WeixinUtil.accessToken!=null){
            Object[] keys= (Object[]) WeixinUtil.accessToken.keySet().toArray();
            if(keys.length>=1)
                firstAccessToken=WeixinUtil.accessToken.get(keys[0]);
        }
        return firstAccessToken;
    }
}
