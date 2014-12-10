package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.message.bean.GroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;

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
     * 发送文本消息，分组群发消息
     *
     * @param groupId 分组id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextGroupMessage(Long groupId, String content);

    /**
     * 发送文本消息，用户列表群发消息
     *
     * @param openid  永固id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextOpenIdMessage(List<String> openid, String content);
}
