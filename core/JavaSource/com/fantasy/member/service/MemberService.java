package com.fantasy.member.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.mvc.error.LoginException;
import com.fantasy.framework.spring.mvc.error.PasswordException;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpCst;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.MemberDetails;
import com.fantasy.member.dao.MemberDao;
import com.fantasy.member.event.context.LoginEvent;
import com.fantasy.member.event.context.LogoutEvent;
import com.fantasy.member.event.context.RegisterEvent;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.UserGroup;
import com.fantasy.security.service.RoleService;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
        if (member == null || encoder.isPasswordValid(member.getPassword(), password, null)) {
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
        if (StringUtil.isNotBlank(member.getPassword()) && !"******".equals(member.getPassword())) {
            Member m = this.memberDao.get(member.getId());
            PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
            if (!encoder.isPasswordValid(m.getPassword(), member.getPassword(), null)) {
                member.setPassword(encoder.encodePassword(member.getPassword(), null));
            }
        } else {
            member.setPassword(null);
        }
        return this.memberDao.save(member);
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
     * 退出
     *
     * @param username 用户名
     */
    public void logout(String username) {
        this.applicationContext.publishEvent(new LogoutEvent(findUniqueByUsername(username)));
    }

    @Cacheable(value = "fantasy.security.memberService", key = "'findUniqueByUsername' + #username ")
    public Member findUniqueByUsername(String username) {
        Member member = this.memberDao.findUniqueBy("username", username);
        if (member == null) {
            return null;
        }
        SpringSecurityUtils.getAuthorities(member);
        return member;
    }

}
