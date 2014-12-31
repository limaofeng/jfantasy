package com.fantasy.wx.core;

import com.fantasy.file.FileItem;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.message.content.*;
import com.fantasy.wx.message.user.Group;
import com.fantasy.wx.message.user.OpenIdList;
import com.fantasy.wx.message.user.User;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.WeiXinSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 微信签名相关接口
 */
public interface WeiXinCoreHelper {

    /**
     * 注册公众号服务，如果账号信息有更改需要重新调用该方法
     *
     * @param accountDetails 账号信息
     */
    public void register(AccountDetails accountDetails);

    /**
     * 解析接收到的消息
     *
     * @param session 微信号session对象
     * @param request HTTP请求
     * @return WeiXinMessage
     * @throws WeiXinException
     */
    public WeiXinMessage parseInMessage(WeiXinSession session, HttpServletRequest request) throws WeiXinException;

    /**
     * 构建回复的消息
     *
     * @param session     微信号session对象
     * @param encryptType encryptType
     * @param message     消息
     * @return String
     * @throws WeiXinException
     */
    public String buildOutMessage(WeiXinSession session, String encryptType, WeiXinMessage message) throws WeiXinException;

    /**
     * 发送图片消息
     *
     * @param session 微信号session对象
     * @param content 图片消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
    public void sendImageMessage(WeiXinSession session, Image content, String toUser) throws WeiXinException;

    /**
     * 发送语音消息
     *
     * @param session 微信号session对象
     * @param content 语音消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
    public void sendVoiceMessage(WeiXinSession session, Voice content, String toUser) throws WeiXinException;

    /**
     * 发送视频消息
     *
     * @param session 微信号session对象
     * @param content 视频消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
    public void sendVideoMessage(WeiXinSession session, Video content, String toUser) throws WeiXinException;

    /**
     * 发送音乐消息
     *
     * @param session 微信号session对象
     * @param content 音乐消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
    public void sendMusicMessage(WeiXinSession session, Music content, String toUser) throws WeiXinException;

    /**
     * 发送音乐消息
     *
     * @param session 微信号session对象
     * @param content 图文消息
     * @param toUsers  接收人
     * @throws WeiXinException
     */
    public void sendNewsMessage(WeiXinSession session, News content, String... toUsers) throws WeiXinException;

    /**
     * 发送文本消息
     *
     * @param session 微信号session对象
     * @param content 文本消息
     * @param toUsers 接收人
     * @throws WeiXinException
     */
    void sendTextMessage(WeiXinSession session, String content, String... toUsers) throws WeiXinException;

    /**
     * 发送文本消息
     *
     * @param session 微信号session对象
     * @param content 文本消息
     * @param toGroup 接收组
     * @throws WeiXinException
     */
    void sendTextMessage(WeiXinSession session, String content, Long toGroup) throws WeiXinException;

    /**
     * 获取分组信息
     *
     * @param session 微信号session对象
     * @return List<Group>
     */
    List<Group> getGroups(WeiXinSession session) throws WeiXinException;

    /**
     * 创建分组信息
     *
     * @param session   微信号session对象
     * @param groupName 分组名称
     */
    Group groupCreate(WeiXinSession session, String groupName) throws WeiXinException;

    /**
     * 更新分组信息
     *
     * @param session   微信号session对象
     * @param groupId   分组Id
     * @param groupName 分组名称
     */
    void groupUpdate(WeiXinSession session, long groupId, String groupName) throws WeiXinException;

    /**
     * @param session 微信号session对象
     * @param userId  用户id
     * @param groupId 分组id
     */
    void userUpdateGroup(WeiXinSession session, String userId, Long groupId) throws WeiXinException;

    /**
     * 获取全部用户关注用户 <br/>
     * 该方法仅在微信粉丝数量有限的情况下，推荐使用
     *
     * @param session 微信号session对象
     * @return List<User>
     */
    List<User> getUsers(WeiXinSession session) throws WeiXinException;

    /**
     * 公众号可通过本接口来获取帐号的关注者列表
     *
     * @param session 微信号session对象
     * @return UserList
     */
    OpenIdList getOpenIds(WeiXinSession session);

    /**
     * 公众号可通过本接口来获取帐号的关注者列表
     *
     * @param session    微信号session对象
     * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
     * @return UserList
     */
    OpenIdList getOpenIds(WeiXinSession session, String nextOpenId);

    /**
     * 获取全部用户关注用户
     *
     * @param session 微信号session对象
     * @param userId  用户id
     * @return List<User>
     */
    User getUser(WeiXinSession session, String userId) throws WeiXinException;

    /**
     * 媒体上传接口
     *
     * @param session   微信号session对象
     * @param mediaType 媒体类型
     * @param fileItem  要上传的文件
     * @return 媒体Id
     */
    String mediaUpload(WeiXinSession session, Media.Type mediaType, FileItem fileItem) throws WeiXinException;

    /**
     * 媒体下载接口
     *
     * @param session 微信号session对象
     * @param mediaId 媒体id
     * @return FileItem
     * @throws WeiXinException
     */
    FileItem mediaDownload(WeiXinSession session, String mediaId) throws WeiXinException;

}
