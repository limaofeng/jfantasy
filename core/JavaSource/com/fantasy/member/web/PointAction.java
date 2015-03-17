package com.fantasy.member.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.Pager.Order;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.bean.Point;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.service.PointService;

/**
 * 积分收支明细
 * @author mingliang
 *
 */
public class PointAction extends ActionSupport{

	private static final long serialVersionUID = 5645930554474803633L;

	@Autowired
	private PointService pointService;
	@Autowired
	private MemberService memberService;
	
	public String index(Long id){
		this.search(new Pager<Point>(),new ArrayList<PropertyFilter>(),id);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(Pager<Point> pager,List<PropertyFilter> filters,Long id){
		filters.add(new PropertyFilter("EQL_member.id",id.toString()));
		if(StringUtil.isBlank(pager.getOrderBy())){
			pager.setOrderBy("id");
			pager.setOrder(Order.desc);
		}
		this.attrs.put(ROOT,this.pointService.findPager(pager, filters));
		this.attrs.put("member",this.memberService.get(id));
		return JSONDATA;
	}
	
	/**
	 * 保存或修改数据
	 * @param consume
	 * @return
	 */
	public String save(Point point){
		this.attrs.put(ROOT, pointService.save(point));
		return JSONDATA;
	}
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.pointService.delete(ids);
		return JSONDATA;
	}
}
