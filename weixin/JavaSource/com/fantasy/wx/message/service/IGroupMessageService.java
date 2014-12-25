package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.bean.GroupMessage;
import com.fantasy.wx.message.bean.GroupNews;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;

import java.io.IOException;
import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IGroupMessageService {
    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<GroupMessage> findPager(Pager<GroupMessage> pager, List<PropertyFilter> filters);

    /**
     * 删除群发消息，只能删除本地，不能删除微信记录
     *
     * @param ids
     */
    public void delete(Long... ids);

    /**
     * 获取分组消息对象
     *
     * @param id 唯一id
     * @return
     */
    public GroupMessage getGroupMessage(Long id);

    /**
     * 保存消息对象
     *
     * @param om
     * @return
     */
    public GroupMessage save(GroupMessage om);

    /**
     * 创建分组群发消息
     *
     * @param groupId 分组id
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassGroupMessage createGroupMessage(Long groupId, String msgType);

    /**
     * 创建用户列表群发消息
     *
     * @param openId  用户id集合
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassOpenIdsMessage createOpenIdsMessage(List<String> openId, String msgType);



    /**
     * 用户列表群发，发送文本消息
     *
     * @param openid  永固id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextOpenIdMessage(List<String> openid, String content);

    /**
     * 用户列表群发，发送图文消息
     * @param openid
     * @param news 群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     * @throws IOException 上传图片失败的io异常
     * @throws com.fantasy.wx.exception.WeiXinException 微信异常
     */
    public int sendNewsOpenIdMessage(List<String> openid,GroupNews news) throws IOException, WeiXinException;
    /**
     * 分组群发，发送文本消息
     *
     * @param groupId 分组id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextGroupMessage(Long groupId, String content);

    /**
     * 分组群发，发送图文消息
     * @param groupId 分组id
     * @param news 群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendNewsGroupMessage(Long groupId,GroupNews news) throws IOException, WeiXinException;

    /**
     * 上传图文消息
     * @param news 图文消息对象
     * @return 上传图文消息的返回对象
     * @throws IOException 文件上传io异常
     * @throws WxErrorException 上传素材的异常
     * @throws com.fantasy.wx.exception.WeiXinException 上传图片的异常
     */
    public WxMpMassUploadResult uploadNews(GroupNews news) throws IOException, WxErrorException, WeiXinException;
}
