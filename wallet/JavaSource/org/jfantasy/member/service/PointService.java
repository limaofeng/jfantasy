package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Point;
import org.jfantasy.member.dao.PointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("mem.PointService")
public class PointService {

    @Autowired
    private PointDao pointDao;

    public Pager<Point> findPager(Pager<Point> pager, List<PropertyFilter> filters) {
        return this.pointDao.findPager(pager, filters);
    }


    /**
     * 保存积分收支明细
     *
     * @param point 积分
     * @return Point
     */
    public Point save(Point point) {
        return this.pointDao.save(point);
    }

    public void delete(Long[] ids) {
        for (Long id : ids) {
            this.pointDao.delete(id);
        }
    }

    /**
     * 保存积分收支明细
     * 必须给出的参数
     *
     * @param title       标题
     * @param status      状态
     * @param score       积分
     * @param memberId    会员ID
     *                    可选参数
     * @param description 说明描述
     * @param url         跳转地址
     */
    public void save(Point.Status status, Integer score, Long memberId, String title, String description, String url) {
        Point point = new Point();
        point.setNotes(title);
        point.setDescription(description);
        point.setPoint(score);
        point.setStatus(status);
        point.getWallet();
        this.save(point);
    }

}
