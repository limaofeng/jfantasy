package com.fantasy.wx.qrcode.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.message.bean.Message;
import com.fantasy.wx.qrcode.bean.QRCode;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository
public class QRCodeDao extends HibernateDao<QRCode, Long> {

}
