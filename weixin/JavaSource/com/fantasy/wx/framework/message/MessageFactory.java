package com.fantasy.wx.framework.message;

import com.fantasy.file.FileItem;
import com.fantasy.framework.util.cglib.CglibUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.content.*;
import com.fantasy.wx.framework.session.WeiXinSession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 微信消息工厂
 */
public class MessageFactory {

    /**
     * 文本消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param content      文本消息内容
     */
    public static TextMessage createTextMessage(Long msgId, String fromUserName, Date createTime, String content) throws WeiXinException {
        TextMessage message = new TextMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        message.setContent(content);
        return message;
    }

    /**
     * 图片消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param url          图片链接
     * @return ImageMessage
     */
    public static ImageMessage createImageMessage(final WeiXinCoreHelper coreHelper, Long msgId, String fromUserName, Date createTime, String mediaId, String url) throws WeiXinException {
        ImageMessage message = new ImageMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        final WeiXinSession session = WeiXinSessionUtils.getCurrentSession();
        Media media = CglibUtil.newInstance(Media.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                try {
                    if ("getFileItem".equalsIgnoreCase(method.getName())) {
                        FileItem fileItem = (FileItem) methodProxy.invokeSuper(o, objects);
                        String id = (String) ClassUtil.getMethodProxy(Media.class, "getId").invoke(o);
                        if (fileItem == null && StringUtil.isNotBlank(id)) {
                            ClassUtil.getMethodProxy(Media.class, "setFileItem", FileItem.class).invoke(o, fileItem = coreHelper.mediaDownload(session, id));
                        }
                        return fileItem;
                    } else {
                        return methodProxy.invokeSuper(o, objects);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return null;
                }
            }
        });
        media.setId(mediaId);
        Image image = new Image(media, url);
        message.setContent(image);

        return message;
    }

    /**
     * 语音消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param format       语音格式，如amr，speex等
     * @return VoiceMessage
     */
    public static VoiceMessage createVoiceMessage(Long msgId, String fromUserName, Date createTime, String mediaId, String format, String recognition) throws WeiXinException {
        VoiceMessage message = new VoiceMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        message.setContent(new Voice(new Media(mediaId, format), recognition));
        return message;
    }

    /**
     * 视频消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param thumbMediaId 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     * @return VideoMessage
     */
    public static VideoMessage createVideoMessage(Long msgId, String fromUserName, Date createTime, String mediaId, String thumbMediaId) throws WeiXinException {
        VideoMessage message = new VideoMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        Video video = new Video(new Media(mediaId), new Media(thumbMediaId));
        message.setContent(video);
        return message;
    }

    /**
     * 地理位置消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param x            地理位置维度
     * @param y            地理位置经度
     * @param scale        地图缩放大小
     * @param label        地理位置信息
     * @return LocationMessage
     */
    public static LocationMessage createLocationMessage(Long msgId, String fromUserName, Date createTime, Double x, Double y, Double scale, String label) throws WeiXinException {
        LocationMessage message = new LocationMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        message.setContent(new Location(x, y, scale, label));
        return message;
    }

    /**
     * 链接消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param title        消息标题
     * @param description  消息描述
     * @param url          消息链接
     * @return LinkMessage
     */
    public static LinkMessage createLinkMessage(Long msgId, String fromUserName, Date createTime, String title, String description, String url) throws WeiXinException {
        LinkMessage message = new LinkMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        message.setContent(new Link(title, description, url));
        return message;
    }


    public static WeiXinMessage createEventMessage(Long msgId, String fromUserName, Date createTime, String event, String eventKey, String ticket, Double latitude, Double longitude, Double precision) throws WeiXinException {
        DefalutEventMessage message = new DefalutEventMessage(msgId, fromUserName, createTime);
        message.setToUserName(WeiXinSessionUtils.getCurrentSession().getAccountDetails().getPrimitiveId());
        if (latitude != null && longitude != null && precision != null) {
            message.setContent(new EventLocation(event, latitude, longitude, precision));
        } else if (eventKey != null && ticket != null) {
            message.setContent(new Event(event, eventKey, ticket));
        } else if (eventKey != null) {
            message.setContent(new Event(event, eventKey));
        } else {
            message.setContent(new Event(event));
        }
        return message;
    }
}
