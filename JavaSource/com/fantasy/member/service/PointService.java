package com.fantasy.member.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.Point;
import com.fantasy.member.dao.PointDao;

@Transactional
@Service("PointService")
public class PointService {
	@Resource
	private PointDao pointDao;

	public List<Point> consume(List<PropertyFilter> filters, int start, int size) {
		List<Point> list = this.pointDao.find(filters, start, size);
		return list;
	}

	public Pager<Point> findPager(Pager<Point> pager,List<PropertyFilter> filters) {
		return this.pointDao.findPager(pager, filters);
	}
	/**
	 * 统计积分收支明细
	 * @param filters
	 * @return
	 */
	public Map<String,Integer> calculateScore(List<PropertyFilter> filters){
		return this.pointDao.calculateScore(filters);
	}
	/**
	 * 统计即将过期的积分
	 * @param filters
	 * @return
	 */
	public Map<String,Integer> calculateWillExpireScore(List<PropertyFilter> filters){
		return this.pointDao.calculateWillExpireScore(filters);
	}
	/**
	 * 保存积分收支明细
	 * @param consume
	 * @return
	 */
	public Point save(Point point) {
		this.pointDao.save(point);
		return point;
	}
	
	public void delete(Long[] ids) {
		for (Long id : ids) {
			this.pointDao.delete(id);
		}
	}
	/**
	 * 保存积分收支明细
	 * 必须给出的参数
	 * @param title 标题
	 * @param status 状态
	 * @param score  积分
	 * @param memberId 会员ID
	 * 可选参数
	 * @param description 说明描述
	 * @param url 跳转地址
	 */
	public void save(Point.Status status, Integer score, Long memberId, String title, String description, String url){
		Point point = new Point();
		point.setTitle(title);
		point.setDescription(description);
		point.setScore(score);
		point.setStatus(status);
		point.setUrl(url);
		point.setMember(new Member());
		point.getMember().setId(memberId);
		this.save(point);
	}
	
}
