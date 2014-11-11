package com.fantasy.member.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.service.MailSendService;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpCst;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.MemberDetails;
import com.fantasy.member.dao.MemberDao;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.UserGroup;
import com.fantasy.security.service.RoleService;
import com.fantasy.system.util.SettingUtil;
import org.hibernate.criterion.Criterion;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 会员管理
 * 
 */
@Service
@Transactional
public class MemberService {

	private final static String DEFAULT_ROLE_CODE = "MEMBER";

	@Resource
	private MemberDao memberDao;
	@Resource
	private RoleService roleService;
	@Resource
	private MailSendService mailSendService;

	/**
	 * 列表查询
	 * 
	 * @param pager
	 *            分页
	 * @param filters
	 *            查询条件
	 * @return
	 */
	public Pager<Member> findPager(Pager<Member> pager, List<PropertyFilter> filters) {
		return this.memberDao.findPager(pager, filters);
	}

	/**
	 * 前台注册页面保存
	 * 
	 * @param member
	 * @return
	 */
	public Member register(Member member) {
		if (member.getDetails() == null) {// 初始化用户信息对象
			member.setDetails(new MemberDetails());
		}
		// 设置默认属性
		member.getDetails().setMailValid(false);
		member.getDetails().setMobileValid(false);
		// 默认昵称与用户名一致
		if (StringUtil.isBlank(member.getNickName())) {
			member.setNickName(member.getUsername());
		}
		// 如果用email注册
		if (RegexpUtil.isMatch(member.getUsername(), RegexpCst.VALIDATOR_EMAIL)) {
			member.getDetails().setEmail(member.getUsername());
		}
		// 如果用手机注册
		if (RegexpUtil.isMatch(member.getUsername(), RegexpCst.VALIDATOR_MOBILE)) {
			member.getDetails().setMobile(member.getUsername());
		}
		// 初始化用户权限
		List<Role> roles = new ArrayList<Role>();
		Role defaultRole = roleService.get(DEFAULT_ROLE_CODE);
		if (defaultRole != null) {
			roles.add(defaultRole);
		}
		member.setRoles(roles);
		member.setUserGroups(new ArrayList<UserGroup>());
		// 保存用户
		this.memberDao.save(member);
		return member;
	}

	/**
	 * 普通用户注册，要填写邮箱
	 * 
	 * @param member
	 */
	public void sendemail(Member member, String url, String title, String ftl) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member", member);
		String requestUrl = SettingUtil.getServerUrl();
		map.put("url", requestUrl + url);
		map.put("path", requestUrl);
		this.mailSendService.sendHtmlEmail(title, ftl, map, member.getDetails().getEmail());
	}

	/**
	 * 验证邮箱是否已被验证使用
	 * 
	 * @param criterions
	 * @return
	 */
	public Member findUnique(Criterion... criterions) {
		return this.memberDao.findUnique(criterions);
	}

	/**
	 * 保存对象
	 * 
	 * @param member
	 * @return
	 */
	@CacheEvict(key = "'findUniqueByUsername' + #member.username ", value = "fantasy.security.memberService")
	public Member save(Member member) {
		if (member.getId() == null) {
			return this.register(member);
		}
		if (!"******".equals(member.getPassword())) {
			Member m = this.memberDao.get(member.getId());
			PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
			if (!encoder.isPasswordValid(m.getPassword(), member.getPassword(), null)) {
				member.setPassword(encoder.encodePassword(member.getPassword(), null));
			}
		} else {
			member.setPassword(null);
		}
		this.memberDao.save(member);
		return member;
	}

	/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
	// @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Member get(Long id) {
		return this.memberDao.get(id);
	}

	/**
	 * 根据id 批量删除
	 * 
	 * @param ids
	 */
	public void delete(Long... ids) {
		for (Long id : ids) {
			this.memberDao.delete(id);
		}
	}

	/**
	 * customer登录
	 * 
	 * @param user
	 */
	public void login(Member user) {
		user.setLastLoginTime(DateUtil.now());
		this.memberDao.update(user);
	}

	/**
	 * customer登出
	 * 
	 * @功能描述
	 * @param user
	 */
	public void logout(Member user) {

	}

	@Cacheable(value = "fantasy.security.memberService", key = "'findUniqueByUsername' + #username ")
	public Member findUniqueByUsername(String username) {
		Member member = this.memberDao.findUniqueBy("username", username);
		if (member == null)
			return member;
		SpringSecurityUtils.getAuthorities(member);
		return member;
	}

}
