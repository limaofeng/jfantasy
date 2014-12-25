package com.fantasy.wx.message.service.impl;

import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.media.bean.WxMedia;
import com.fantasy.wx.media.service.IMediaService;
import com.fantasy.wx.message.bean.GroupMessage;
import com.fantasy.wx.message.bean.GroupNews;
import com.fantasy.wx.message.bean.GroupNewsArticle;
import com.fantasy.wx.message.dao.GroupNewsArticleDao;
import com.fantasy.wx.message.dao.GroupNewsDao;
import com.fantasy.wx.message.dao.OutMessageDao;
import com.fantasy.wx.message.service.IGroupMessageService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class GroupMessageService implements IGroupMessageService {
    @Resource
    private OutMessageDao outMessageDao;
    @Resource
    private WeixinConfigInit config;
    @Resource
    private FileManagerFactory factory;
    @Resource
    private GroupNewsDao groupNewsDao;
    @Resource
    private GroupNewsArticleDao groupNewsArticleDao;
    private IMediaService mediaService;

    @Override
    public Pager<GroupMessage> findPager(Pager<GroupMessage> pager, List<PropertyFilter> filters) {
        return this.outMessageDao.findPager(pager, filters);
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            outMessageDao.delete(id);
        }
    }

    @Override
    public GroupMessage getGroupMessage(Long id) {
        return outMessageDao.get(id);
    }

    @Override
    public GroupMessage save(GroupMessage om) {
        this.outMessageDao.save(om);
        return om;
    }

    @Override
    public WxMpMassGroupMessage createGroupMessage(Long groupId, String msgType) {
        WxMpMassGroupMessage groupM = new WxMpMassGroupMessage();
        groupM.setGroupId(groupId);
        groupM.setMsgtype(msgType);
        return groupM;
    }

    @Override
    public WxMpMassOpenIdsMessage createOpenIdsMessage(List<String> openId, String msgType) {
        WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
        openIdsMessage.setMsgType(msgType);
        for (String s : openId) {
            openIdsMessage.addUser(s);
        }
        return openIdsMessage;
    }

    @Override
    public int sendTextOpenIdMessage(List<String> openid, String content) {
        WxMpMassOpenIdsMessage message = createOpenIdsMessage(openid, WxConsts.MASS_MSG_TEXT);
        message.setContent(content);
        return sendOpenIdMessage(message);
    }
    @Override
    public int sendNewsOpenIdMessage(List<String> openid,GroupNews news) throws  IOException, WeiXinException {
        WxMpMassOpenIdsMessage message=createOpenIdsMessage(openid, WxConsts.MASS_MSG_NEWS);
        try {
            //上传图文素材
            WxMpMassUploadResult result=uploadNews(news);
            message.setMediaId(result.getMediaId());
            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
            return Integer.parseInt(massResult.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }
    @Override
    public int sendTextGroupMessage(Long groupId, String content) {
        WxMpMassGroupMessage groupM = createGroupMessage(groupId, WxConsts.MASS_MSG_TEXT);
        groupM.setContent(content);
        return sendGroupMessage(groupM);
    }
    @Override
    public int sendNewsGroupMessage(Long groupId,GroupNews news) throws IOException, WeiXinException {
        WxMpMassGroupMessage message = createGroupMessage(groupId, WxConsts.MASS_MSG_TEXT);
        try {
            //上传图文素材
            WxMpMassUploadResult result=uploadNews(news);
            message.setMediaId(result.getMediaId());
            WxMpMassSendResult massResult = config.getUtil().massGroupMessageSend(message);
            return Integer.parseInt(massResult.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }

    }

    public WxMpMassUploadResult uploadNews(GroupNews news) throws IOException, WxErrorException, WeiXinException {
        groupNewsDao.save(news);
        //生成第三方库的群发对象
        WxMpMassNews massNews=new WxMpMassNews();
        for(GroupNewsArticle art:news.getArticles()){
            WxMedia media=mediaService.mediaUpload(art.getThumbFile(), WxConsts.MEDIA_IMAGE);
            media.setThumbMediaId(media.getMediaId());
            WxMpMassNews.WxMpMassNewsArticle article = BeanUtil.copyProperties(new WxMpMassNews.WxMpMassNewsArticle(),art);
            massNews.addArticle(article);
            groupNewsArticleDao.save(art);
        }
        WxMpMassUploadResult result=config.getUtil().massNewsUpload(massNews);
        news.setCreatedAt(result.getCreatedAt());
        news.setType(result.getType());
        news.setMediaId(result.getMediaId());
        groupNewsDao.save(news);
        return result;
    }


    /**
     * 按照分组发送群发消息
     * @param message
     * @return
     */
    public int sendGroupMessage(WxMpMassGroupMessage message){
        save(BeanUtil.copyProperties(new GroupMessage(), message));
        try {
            WxMpMassSendResult result = config.getUtil().massGroupMessageSend(message);
            return Integer.parseInt(result.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }

    /**
     * 按照openid发送群发消息
     * @param message
     * @return
     */
    public int sendOpenIdMessage(WxMpMassOpenIdsMessage message){
        save(BeanUtil.copyProperties(new GroupMessage(), message));
        try {
            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
            return Integer.parseInt(massResult.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }
}
