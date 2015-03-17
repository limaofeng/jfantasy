package com.fantasy.wx.service;

import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.bean.GroupMessage;
import com.fantasy.wx.bean.GroupNews;
import com.fantasy.wx.bean.GroupNewsArticle;
import com.fantasy.wx.bean.Media;
import com.fantasy.wx.dao.GroupMessageDao;
import com.fantasy.wx.dao.GroupNewsArticleDao;
import com.fantasy.wx.dao.GroupNewsDao;
import com.fantasy.wx.framework.exception.WeiXinException;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class GroupMessageWeiXinService {
    @Autowired
    private GroupMessageDao groupMessageDao;
    @Autowired
    private FileManagerFactory factory;
    @Autowired
    private GroupNewsDao groupNewsDao;
    @Autowired
    private GroupNewsArticleDao groupNewsArticleDao;
    @Autowired
    private MediaWeiXinService mediaWeiXinService;

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<GroupMessage> findPager(Pager<GroupMessage> pager, List<PropertyFilter> filters) {
        return this.groupMessageDao.findPager(pager, filters);
    }

    /**
     * 删除群发消息，只能删除本地，不能删除微信记录
     *
     * @param ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            groupMessageDao.delete(id);
        }
    }

    /**
     * 获取分组消息对象
     *
     * @param id 唯一id
     * @return
     */
    public GroupMessage getGroupMessage(Long id) {
        return groupMessageDao.get(id);
    }

    /**
     * 保存消息对象
     *
     * @param om
     * @return
     */
    public GroupMessage save(GroupMessage om) {
        this.groupMessageDao.save(om);
        return om;
    }

    /**
     * 创建分组群发消息
     *
     * @param groupId 分组id
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassGroupMessage createGroupMessage(Long groupId, String msgType) {
        WxMpMassGroupMessage groupM = new WxMpMassGroupMessage();
        groupM.setGroupId(groupId);
        groupM.setMsgtype(msgType);
        return groupM;
    }

    /**
     * 创建用户列表群发消息
     *
     * @param openId  用户id集合
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassOpenIdsMessage createOpenIdsMessage(List<String> openId, String msgType) {
        WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
        openIdsMessage.setMsgType(msgType);
        for (String s : openId) {
            openIdsMessage.addUser(s);
        }
        return openIdsMessage;
    }

    /**
     * 用户列表群发，发送文本消息
     *
     * @param openid  永固id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextOpenIdMessage(List<String> openid, String content) {
        WxMpMassOpenIdsMessage message = createOpenIdsMessage(openid, WxConsts.MASS_MSG_TEXT);
        message.setContent(content);
        return sendOpenIdMessage(message);
    }

    /**
     * 用户列表群发，发送图文消息
     *
     * @param openid
     * @param news   群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     * @throws IOException                              上传图片失败的io异常
     * @throws com.fantasy.wx.framework.exception.WeiXinException 微信异常
     */
    public int sendNewsOpenIdMessage(List<String> openid, GroupNews news) throws IOException, WeiXinException {
        WxMpMassOpenIdsMessage message = createOpenIdsMessage(openid, WxConsts.MASS_MSG_NEWS);
//        try {
//            //上传图文素材
//            WxMpMassUploadResult result = uploadNews(news);
//            message.setMediaId(result.getMediaId());
//            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
//            return Integer.parseInt(massResult.getErrorCode());
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//            return e.getError().getErrorCode();
//        }
        return -1;
    }

    /**
     * 分组群发，发送文本消息
     *
     * @param groupId 分组id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextGroupMessage(Long groupId, String content) {
        WxMpMassGroupMessage groupM = createGroupMessage(groupId, WxConsts.MASS_MSG_TEXT);
        groupM.setContent(content);
        return sendGroupMessage(groupM);
    }

    /**
     * 分组群发，发送图文消息
     *
     * @param groupId 分组id
     * @param news    群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendNewsGroupMessage(Long groupId, GroupNews news) throws IOException, WeiXinException {
        WxMpMassGroupMessage message = createGroupMessage(groupId, WxConsts.MASS_MSG_TEXT);
            //上传图文素材
//            WxMpMassUploadResult result = uploadNews(news);
//            message.setMediaId(result.getMediaId());
//            WxMpMassSendResult massResult = config.getUtil().massGroupMessageSend(message);
//            return Integer.parseInt(massResult.getErrorCode());

            return -1;

    }

    /**
     * 上传图文消息
     *
     * @param news 图文消息对象
     * @return 上传图文消息的返回对象
     * @throws IOException                              文件上传io异常
     * @throws WxErrorException                         上传素材的异常
     * @throws com.fantasy.wx.framework.exception.WeiXinException 上传图片的异常
     */
    public WxMpMassUploadResult uploadNews(GroupNews news) throws IOException, WxErrorException, WeiXinException {
        groupNewsDao.save(news);
        //生成第三方库的群发对象
        WxMpMassNews massNews = new WxMpMassNews();
        for (GroupNewsArticle art : news.getArticles()) {
            Media media = mediaWeiXinService.mediaUpload(art.getThumbFile(), WxConsts.MEDIA_IMAGE);
            media.setThumbMediaId(media.getMediaId());
            WxMpMassNews.WxMpMassNewsArticle article = BeanUtil.copyProperties(new WxMpMassNews.WxMpMassNewsArticle(), art);
            massNews.addArticle(article);
            groupNewsArticleDao.save(art);
        }
//        WxMpMassUploadResult result = config.getUtil().massNewsUpload(massNews);
//        news.setCreatedAt(result.getCreatedAt());
//        news.setType(result.getType());
//        news.setMediaId(result.getMediaId());
//        groupNewsDao.save(news);
        return null;
    }


    /**
     * 按照分组发送群发消息
     *
     * @param message
     * @return
     */
    public int sendGroupMessage(WxMpMassGroupMessage message) {
        save(BeanUtil.copyProperties(new GroupMessage(), message));
//            WxMpMassSendResult result = config.getUtil().massGroupMessageSend(message);
//            return Integer.parseInt(result.getErrorCode());
        return -1;
    }

    /**
     * 按照openid发送群发消息
     *
     * @param message
     * @return
     */
    public int sendOpenIdMessage(WxMpMassOpenIdsMessage message) {
        save(BeanUtil.copyProperties(new GroupMessage(), message));
//            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
//            return Integer.parseInt(massResult.getErrorCode());
            return -1;
    }
}
