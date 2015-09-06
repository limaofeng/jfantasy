package com.fantasy.wx.framework.message;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 微信消息接口
 */
@ApiModel(value = "微信消息接口")
public interface WeiXinMessage<T> {

    /**
     * MsgId	消息id，64位整型
     *
     * @return id
     */
    @ApiModelProperty("消息id，64位整型")
    Long getId();

    /**
     * 发送方帐号（一个OpenID）
     *
     * @return String
     */
    @ApiModelProperty("发送方帐号（一个OpenID/微信原始ID）")
    String getFromUserName();

    /**
     * 消息创建时间 （整型）
     *
     * @return date
     */
    @ApiModelProperty("消息创建时间")
    Date getCreateTime();

    /**
     * 获取微信内容
     *
     * @return T
     */
    @ApiModelProperty(value = "微信内容", dataType = "具体类型参考其实现类")
    T getContent();

    /**
     * 开发者微信号 (微信原始ID)
     *
     * @return String
     */
    @ApiModelProperty("接收方帐号（一个OpenID/微信原始ID）")
    String getToUserName();

}
