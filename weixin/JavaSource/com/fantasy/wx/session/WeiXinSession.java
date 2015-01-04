package com.fantasy.wx.session;


import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.message.content.*;
import com.fantasy.wx.message.user.User;
import com.fantasy.wx.oauth2.Scope;

import java.util.List;

/**
 * 微信 session 接口
 * 主要包含微信消息相关的公众号及订阅号内容
 */
public interface WeiXinSession {

    /**
     * 微信号的 appid
     *
     * @return String
     */
    String getId();

    /**
     * 发送图片消息
     *
     * @param content 图片消息
     * @param toUser  接收人
     */
    public void sendImageMessage(Image content, String toUser);

    /**
     * 发送语音消息
     *
     * @param content 语音消息
     * @param toUser  接收人
     */
    public void sendVoiceMessage(Voice content, String toUser);

    /**
     * 发送视频消息
     *
     * @param content 视频消息
     * @param toUser  接收人
     */
    public void sendVideoMessage(Video content, String toUser);

    /**
     * 发送音乐消息
     *
     * @param content 音乐消息
     * @param toUser  接收人
     */
    public void sendMusicMessage(Music content, String toUser);

    /**
     * 发送图文消息
     *
     * @param content 图文消息
     * @param toUser  接收人
     */
    public void sendNewsMessage(News content, String toUser);

    /**
     * 发送图文消息
     *
     * @param content 图文消息列表
     * @param toUsers 接收人
     */
    public void sendNewsMessage(List<Article> content, String... toUsers);

    /**
     * 发送文本消息
     *
     * @param content 文本消息
     * @param toUsers 接收人
     */
    void sendTextMessage(String content, String... toUsers);

    /**
     * 发送文本消息
     *
     * @param content 文本消息
     * @param toGroup 接收组
     */
    void sendTextMessage(String content, Long toGroup);

    /**
     * 获取安全链接
     *
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * @return url
     */
    String getAuthorizationUrl(String redirectUri, Scope scope);

    /**
     * 获取安全链接
     *
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * @param state       重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
     * @return url
     */
    String getAuthorizationUrl(String redirectUri, Scope scope, String state);

    /**
     * 获取安全连接的授权用户
     *
     * @param code 安全链接code
     * @return User
     */
    User getUser(String code);

    /**
     * 获取关注的粉丝
     * @return List<User>
     */
    List<User> getUsers();

    /**
     * 获取当前公众号信息
     *
     * @return AccountDetails
     */
    AccountDetails getAccountDetails();

    /**
     * 微信第三方框架
     *
     * @return WeiXinCoreHelper
     */
    WeiXinCoreHelper getWeiXinCoreHelper();

}
