package com.fantasy.wx.qrcode.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.qrcode.bean.QRCode;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IQRCodeService {
    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<QRCode> findPager(Pager<QRCode> pager, List<PropertyFilter> filters);

    /**
     * 删除二维码对象
     *
     * @param ids
     */
    public void delete(Long... ids);

    /**
     * 获取二维码对象
     *
     * @param id
     * @return
     */
    public QRCode getQRCode(Long id);

    /**
     * 保存二维码对象
     *
     * @param q
     * @return
     */
    public QRCode save(QRCode q);

    /**
     * 换取临时二维码ticket
     *
     * @param linkKey 关联id
     * @param e       有效时间
     * @return 二维码对象
     * @throws
     */
    public QRCode qrCodeCreateTmpTicket(String linkKey, Integer e) throws WxException;

    /**
     * 换取永久二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     *
     * @param linkKey 关联id
     * @return 二维码对象
     * @throws com.fantasy.wx.exception.WxException
     */
    public QRCode qrCodeCreateLastTicket(String linkKey) throws WxException;
}
