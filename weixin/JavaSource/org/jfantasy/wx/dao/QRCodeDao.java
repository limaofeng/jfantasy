package org.jfantasy.wx.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.wx.bean.QRCode;
import org.springframework.stereotype.Repository;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Repository("wx.QRCodeDao")
public class QRCodeDao extends HibernateDao<QRCode, Long> {

}
