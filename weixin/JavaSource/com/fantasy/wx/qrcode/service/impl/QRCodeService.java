package com.fantasy.wx.qrcode.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.qrcode.bean.QRCode;
import com.fantasy.wx.qrcode.dao.QRCodeDao;
import com.fantasy.wx.qrcode.service.IQRCodeService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class QRCodeService implements IQRCodeService {
    @Resource
    private QRCodeDao qrCodeDao;
    @Resource
    private WeixinConfigInit config;

    public Pager<QRCode> findPager(Pager<QRCode> pager, List<PropertyFilter> filters) {
        return this.qrCodeDao.findPager(pager, filters);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            qrCodeDao.delete(id);
        }
    }

    public QRCode getQRCode(Long id) {
        return qrCodeDao.get(id);
    }

    public QRCode save(QRCode q) {
        q.setCreateTime(new Date());
        this.qrCodeDao.save(q);
        return q;
    }

    public QRCode qrCodeCreateTmpTicket(String linkKey, Integer e) throws WxException {
        if (e == null) e = 1800;
        QRCode code = new QRCode();
        qrCodeDao.save(code);
        try{
            WxMpQrCodeTicket ticket = config.getUtil().qrCodeCreateTmpTicket(new Long(code.getId()).intValue(), e);
            copyBean(ticket,code,linkKey);
            save(code);
        }catch (WxErrorException exception){
            throw WxException.wxExceptionBuilder(exception);
        }


        return code;
    }

    public QRCode qrCodeCreateLastTicket(String linkKey) throws WxException {
        QRCode code = new QRCode();
        qrCodeDao.save(code);
        try{
            WxMpQrCodeTicket ticket= config.getUtil().qrCodeCreateLastTicket(new Long(code.getId()).intValue());
            copyBean(ticket,code,linkKey);
            save(code);
        }catch (WxErrorException exception){
            throw WxException.wxExceptionBuilder(exception);
        }


        return code;
    }

    public void copyBean(WxMpQrCodeTicket ticket,QRCode code,String linkKey){
        code.setExpireSeconds(ticket.getExpire_seconds());
        code.setImgPath("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket.getTicket());
        code.setUrl(ticket.getUrl());
        code.setLinkKey(linkKey);
    }
}
