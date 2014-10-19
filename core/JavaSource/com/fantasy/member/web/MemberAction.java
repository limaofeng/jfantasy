package com.fantasy.member.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员操作action
 * 
 * @功能描述 该类方法为管理端方法
 * @author 李茂峰
 * @since 2013-12-17 上午10:22:06
 * @version 1.0
 */
public class MemberAction extends ActionSupport {

	private static final long serialVersionUID = 8858968932197738213L;

	@Resource
	private MemberService memberService;
    /*
	@Resource
	private CartService cartService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private OrderService orderService;

	@Resource
	private ReceiverService receiverService;
	*/

	/**
	 * 会员首页
	 * 
	 * @return
	 */
	public String index() {
		this.search(new Pager<Member>(), new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 会员搜索列表
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Member> pager, List<PropertyFilter> filters) {
		if(StringUtil.isNotBlank(pager.getOrderBy())){
			pager.setOrderBy("createTime");
			pager.setOrder(Pager.Order.desc);
		}
		pager = memberService.findPager(pager, filters);
		this.attrs.put(ROOT, pager);
		return JSONDATA;
	}

	/**
	 * 会员购物车搜索
	 * 
	 * @param pager
	 * @param filters
	 * @return

	public String cartsearch(Pager<CartItem> pager, List<PropertyFilter> filters) {
		filters.add(new PropertyFilter("EQE_cart.ownerType", OwnerType.Member));
		this.attrs.put("cartItemPager", this.cartService.findPager(pager, filters));
		return JSONDATA;
	}*/

	/**
	 * 会员保存
	 * 
	 * @param member 会员对象
	 * @return {String}
	 */
	public String save(Member member) {
		this.attrs.put(ROOT, memberService.save(member));
		return JSONDATA;
	}

	/**
	 * 会员修改
	 * 
	 * @param id 编辑会员信息
	 * @return {String}
	 */
	public String edit(Long id) {
		Member member = memberService.get(id);
		this.attrs.put("member", member);
		return SUCCESS;
	}

	/**
	 * 会员显示
	 * 
	 * @param id 会员Id
	 * @return {String}
	 */
	public String view(Long id) {
		Member member = memberService.get(id);
		this.attrs.put("member", member);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		// 会员订单
		filters.add(new PropertyFilter("EQL_member.id", id + ""));
		//this.attrs.put("orderPager", this.orderService.findPager(new Pager<Order>(), filters));
		// 会员购物车
		//filters.clear();
		//filters.add(new PropertyFilter("EQE_cart.ownerType", OwnerType.Member));
		//filters.add(new PropertyFilter("EQS_cart.owner", member.getUsername()));
		//this.attrs.put("cartItemPager", this.cartService.findPager(new Pager<CartItem>(), filters));
		// 收货地址
		//this.attrs.put("receivers", this.receiverService.find(new Criterion[] { Restrictions.eq("member.id", id) }, "isDefault", "desc"));
		return SUCCESS;
	}

	/**
	 * 会员删除
	 * 
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.memberService.delete(ids);
		return JSONDATA;
	}

	/**
	 * 会员收藏
	 * 
	 * @param id
	 * @return

	public String favorite(String id) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQL_favoriteMembers.id", id));
		this.attrs.put("pager", goodsService.findPager(new Pager<Goods>(), filters));
		return SUCCESS;
	}*/

}
