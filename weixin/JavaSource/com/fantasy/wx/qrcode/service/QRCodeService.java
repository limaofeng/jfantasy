package com.fantasy.wx.qrcode.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.OutMessage;
import com.fantasy.wx.message.dao.OutMessageDao;
import com.fantasy.wx.qrcode.bean.QRCode;
import com.fantasy.wx.qrcode.dao.QRCodeDao;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class QRCodeService {
    @Resource
    private QRCodeDao qrCodeDao;
    @Resource
    private WeixinConfigInit config;

    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<QRCode> findPager(Pager<QRCode> pager, List<PropertyFilter> filters) {
        return this.qrCodeDao.findPager(pager, filters);
    }
    public void delete(Long... ids){
        for(Long id:ids){
            qrCodeDao.delete(id);
        }
    }
    public QRCode getQRCode(Long id){
        return qrCodeDao.get(id);
    }
    public QRCode save(QRCode q){
        this.qrCodeDao.save(q);
        return q;
    }
    /**
     * <pre>
     * 换取临时二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     * </pre>
     * @throws WxErrorException
     */
    public QRCode qrCodeCreateTmpTicket() throws WxErrorException {
        QRCode code=new QRCode();
        qrCodeDao.save(code);
        WxMpQrCodeTicket ticket = config.getUtil().qrCodeCreateTmpTicket(new Long(code.getId()).intValue(), 1800);
        code.setExpireSeconds(ticket.getExpire_seconds());
        code.setTicket(ticket.getTicket());
        code.setUrl(ticket.getUrl());
        qrCodeDao.save(code);
        return code;
    }
    /**
     * <pre>
     * 换取永久二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     * </pre>
     * @return
     * @throws WxErrorException
     */
    public QRCode qrCodeCreateLastTicket() throws WxErrorException {
        QRCode code=new QRCode();
        qrCodeDao.save(code);
        WxMpQrCodeTicket ticket = config.getUtil().qrCodeCreateLastTicket(new Long(code.getId()).intValue());
        code.setExpireSeconds(ticket.getExpire_seconds());
        code.setTicket(ticket.getTicket());
        code.setUrl(ticket.getUrl());
        qrCodeDao.save(code);
        return code;
    }

}
