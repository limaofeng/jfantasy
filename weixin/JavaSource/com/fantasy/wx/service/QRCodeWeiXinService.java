package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.bean.QRCode;
import com.fantasy.wx.dao.QRCodeDao;
import com.fantasy.wx.framework.exception.WeiXinException;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 二维码操作类
 */
@Service
@Transactional
public class QRCodeWeiXinService {

    @Resource
    private QRCodeDao qrCodeDao;
    @Resource
    private WeixinConfigInit config;

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<QRCode> findPager(Pager<QRCode> pager, List<PropertyFilter> filters) {
        return this.qrCodeDao.findPager(pager, filters);
    }

    /**
     * 删除二维码对象
     *
     * @param ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            qrCodeDao.delete(id);
        }
    }

    /**
     * 获取二维码对象
     *
     * @param id
     * @return
     */
    public QRCode getQRCode(Long id) {
        return qrCodeDao.get(id);
    }

    /**
     * 保存二维码对象
     *
     * @param q
     * @return
     */
    public QRCode save(QRCode q) {
        q.setCreateTime(new Date());
        this.qrCodeDao.save(q);
        return q;
    }

    /**
     * 换取临时二维码ticket
     *
     * @param linkKey 关联id
     * @param e       有效时间
     * @return 二维码对象
     * @throws
     */
    public QRCode qrCodeCreateTmpTicket(String linkKey, Integer e) throws WeiXinException {
        if (e == null) e = 1800;
        QRCode code = new QRCode();
        qrCodeDao.save(code);
        try {
            WxMpQrCodeTicket ticket = config.getUtil().qrCodeCreateTmpTicket(new Long(code.getId()).intValue(), e);
            copyBean(ticket, code, linkKey);
            save(code);
        } catch (WxErrorException exception) {
            throw WeiXinException.wxExceptionBuilder(exception);
        }
        return code;
    }

    /**
     * 换取永久二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     *
     * @param linkKey 关联id
     * @return 二维码对象
     * @throws com.fantasy.wx.framework.exception.WeiXinException
     */
    public QRCode qrCodeCreateLastTicket(String linkKey) throws WeiXinException {
        QRCode code = new QRCode();
        qrCodeDao.save(code);
        try {
            WxMpQrCodeTicket ticket = config.getUtil().qrCodeCreateLastTicket(code.getId().intValue());
            copyBean(ticket, code, linkKey);
            save(code);
        } catch (WxErrorException exception) {
            throw WeiXinException.wxExceptionBuilder(exception);
        }


        return code;
    }

    public void copyBean(WxMpQrCodeTicket ticket, QRCode code, String linkKey) {
        code.setExpireSeconds(ticket.getExpire_seconds());
        code.setImgPath("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket.getTicket());
        code.setUrl(ticket.getUrl());
        code.setLinkKey(linkKey);
    }
}
