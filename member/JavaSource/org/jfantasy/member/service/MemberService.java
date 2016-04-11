package org.jfantasy.member.service;

import org.hibernate.criterion.Criterion;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.LoginException;
import org.jfantasy.framework.spring.mvc.error.PasswordException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpCst;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.MemberDetails;
import org.jfantasy.member.dao.MemberDao;
import org.jfantasy.member.event.LoginEvent;
import org.jfantasy.member.event.LogoutEvent;
import org.jfantasy.member.event.RegisterEvent;
import org.jfantasy.security.SpringSecurityUtils;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.UserGroup;
import org.jfantasy.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员管理
 */
@Service
@Transactional
public class MemberService {

    private final static String DEFAULT_ROLE_CODE = "MEMBER";

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return Pager<Member>
     */
    public Pager<Member> findPager(Pager<Member> pager, List<PropertyFilter> filters) {
        return this.memberDao.findPager(pager, filters);
    }

    /**
     * 会员登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return Member
     */
    public Member login(String username, String password) {
        Member member = this.memberDao.findUniqueBy("username", username);
        if (member == null || !passwordEncoder.isPasswordValid(member.getPassword(), password, null)) {
            throw new PasswordException("用户名和密码错误");
        }
        if (!member.isEnabled()) {
            throw new LoginException("用户被禁用");
        }
        if (!member.isAccountNonLocked()) {
            throw new LoginException("用户被锁定");
        }
        member.setLastLoginTime(DateUtil.now());
        this.memberDao.save(member);
        this.applicationContext.publishEvent(new LoginEvent(member));
        return member;
    }

    /**
     * 前台注册页面保存
     *
     * @param member 注册信息
     * @return Member
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
        applicationContext.publishEvent(new RegisterEvent(member = this.memberDao.save(member)));
        return member;
    }

    public List<Member> find(Criterion... criterions) {
        return this.memberDao.find(criterions);
    }

    /**
     * 验证邮箱是否已被验证使用
     *
     * @param criterions
     * @return Member
     */
    public Member findUnique(Criterion... criterions) {
        return this.memberDao.findUnique(criterions);
    }

    /**
     * 保存对象
     *
     * @param member member
     * @return Member
     */
    @CacheEvict(key = "'findUniqueByUsername' + #member.username ", value = "fantasy.security.memberService")
    public Member save(Member member) {
        if (member.getId() == null) {
            return this.register(member);
        }
        if (StringUtil.isNotBlank(member.getPassword()) && !"******".equals(member.getPassword())) {
            Member m = this.memberDao.get(member.getId());
            if (!passwordEncoder.isPasswordValid(m.getPassword(), member.getPassword(), null)) {
                member.setPassword(passwordEncoder.encodePassword(member.getPassword(), null));
            }
        } else {
            member.setPassword(null);
        }
        return this.memberDao.save(member);
    }

    /**
     * 获取对象
     *
     * @param id id
     * @return Member
     */
    public Member get(Long id) {
        return this.memberDao.get(id);
    }

    /**
     * 根据id 批量删除
     *
     * @param ids ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.memberDao.delete(id);
        }
    }

    /**
     * 退出
     *
     * @param username 用户名
     */
    public void logout(String username) {
        this.applicationContext.publishEvent(new LogoutEvent(findUniqueByUsername(username)));
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return Member
     */
    public Member findUniqueByUsername(String username) {
        Member member = this.memberDao.findUniqueBy("username", username);
        if (member == null) {
            return null;
        }
        SpringSecurityUtils.getAuthorities(member);
        return member;
    }

}
