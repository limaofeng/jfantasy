package com.fantasy.member.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;

import javax.annotation.Resource;

public class MemberCenterAction extends ActionSupport {
	
	private static final long serialVersionUID = 1187554261166469122L;

	@Autowired
	private MemberService memberService;// 会员

	/**
	 * 会员登录中心
	 * 
	 * @return string
     */
	public String center() {
		return SUCCESS;
	}

	/**
	 * 前台基本信息提交页面
	 * 
	 * @return {string}
     */
	public String memberSave(Member member) {
		// 前台只能修改自己的信息
		MemberUser memberUser = SpringSecurityUtils.getCurrentUser(MemberUser.class);
		member.setId(memberUser.getUser().getId());
		if (member.getDetails() == null) {
			member.setDetails(memberUser.getUser().getDetails());
		}
		member.getDetails().setMemberId(member.getId());
		// 将不能修改的字段设置为NULL
		member.getDetails().setMailValid(null);
		member.getDetails().setMobileValid(null);
		// 保存
		this.memberService.save(member);
		// 更新当前用户session中的信息
		SpringSecurityUtils.getCurrentUser(MemberUser.class).setUser(member);
		this.attrs.put(ROOT, member);
		return JSONDATA;
	}

	/**
	 * 我的订单列表
	 *
	 * @return
	public String order(Pager<Order> pager, List<PropertyFilter> filters) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		// 强制排序
		pager.setOrderBy("createTime");
		pager.setOrder(Pager.Order.desc);
		pager.setPageSize(5);
		// 只返回当前用户的订单
		filters.add(new PropertyFilter("EQL_member.id", member.getId() + ""));
		// 直接查询
		this.attrs.put("pager", this.orderService.findPager(pager, filters));
		return SUCCESS;
	}

	 * 我的收藏列表
	 * 
	 * @return

	public String favorite() {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQL_favoriteMembers.id", member.getId() + ""));
		Pager<Goods> pager = new Pager<Goods>();
		pager.setOrderBy("createTime");
		pager.setOrder(Pager.Order.desc);
		this.attrs.put("pager", this.goodsService.findPager(pager, filters));
		return SUCCESS;
	}

	 * 删除我的收藏操作
	 * 
	 * @param ids
	 * @return

	public String favoriteDel(Long... ids) {
		Member member = memberService.get(SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser().getId());
		for (Long id : ids) {
			ObjectUtil.remove(member.getFavoriteGoods(), "id", id);
		}
		memberService.save(member);
		return JSONDATA;
	}

	 * 我的收获地址
	 * 
	 * @return

	public String receiver() {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		this.attrs.put("member", member);
		this.attrs.put("receivers", this.receiverService.find(new Criterion[] { Restrictions.eq("member.id", member.getId()) }, "isDefault", "desc"));
		return SUCCESS;
	}*/

	/**
	 * 添加我的收获地址
	 * 
	 * @return

	public String receiverSave(Receiver receiver) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		receiver.setMember(member);
		this.attrs.put(ROOT, this.receiverService.save(receiver));
		return JSONDATA;
	}*/

	/**
	 * 删除我的收获地址
	 * 
	 * @param receiver
	 * @return

	public String receiverDel(Receiver receiver) {
		this.receiverService.deltele(receiver.getId());
		return JSONDATA;
	}*/

	/**
	 * 设置为默认收获地址
	 * 
	 * @param receiver
	 * @return

	public String receiverDef(Long id) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		List<Receiver> receivers = this.receiverService.find(new Criterion[] { Restrictions.eq("member.id", member.getId()) }, "isDefault", "desc");
		Receiver receiver = null;

		while (null != (receiver = ObjectUtil.find(receivers, "isDefault", true))) {
			receiver.setIsDefault(false);
			this.receiverService.save(receiver);
		}

		receiver = ObjectUtil.find(receivers, "id", id);
		receiver.setIsDefault(true);
		this.receiverService.save(receiver);

		this.attrs.put(ROOT, ObjectUtil.sort(receivers, "isDefault", "desc"));
		return JSONDATA;
	}

	 * 我的评论
	 * 
	 * @return
	public String comment() {
		return SUCCESS;
	}

	/**
	 * 我的问答
	 * 
	 * @return

	public String problem() {
		return SUCCESS;
	}
     */
}
