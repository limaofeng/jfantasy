package org.jfantasy.pay.bean.enums;

/**
 * 发行卡的批次状态
 */
public enum CardDesignStatus {
    /**
     * 草稿
     */
    draft,
    /**
     * 发布
     */
    publish,
    /**
     * 取消发布(该状态并不真实存在于数据库中,只是一个动作,取消发布后数据会变成草稿状态)
     */
    unpublish,
    /**
     * 销毁
     */
    destroyed
}
