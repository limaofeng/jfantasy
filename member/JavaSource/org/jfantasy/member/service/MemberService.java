package org.jfantasy.member.service;

import org.hibernate.criterion.Criterion;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpCst;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.MemberDetails;
import org.jfantasy.member.dao.MemberDao;
import org.jfantasy.member.event.LoginEvent;
import org.jfantasy.member.event.LogoutEvent;
import org.jfantasy.member.event.RegisterEvent;
import org.jfantasy.member.service.vo.AuthType;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.UserGroup;
import org.jfantasy.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public Member login(AuthType type, String username, String password) {
        Member member = this.memberDao.findUniqueBy("username", username);
        switch (type) {
            case password:
                if (member == null || !passwordEncoder.matches(member.getPassword(), password)) {
                    throw new ValidationException(203.1f, "用户名和密码错误");
                }
                break;
            case macode:
                break;
        }
        if (!member.getEnabled()) {
            throw new ValidationException(203.1f, "用户被禁用");
        }
        if (!member.getAccountNonLocked()) {
            throw new ValidationException(203.1f, "用户被锁定");
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
    public Member save(Member member) {
        if (Member.MEMBER_TYPE_PERSONAL.equals(member.getType())) {
            MemberDetails details = ObjectUtil.defaultValue(member.getDetails(), new MemberDetails());
            // 设置默认属性
            details.setMailValid(false);
            details.setMobileValid(false);
            details.setLevel(0L);
            // 如果用email注册
            if (RegexpUtil.isMatch(member.getUsername(), RegexpCst.VALIDATOR_EMAIL)) {
                details.setEmail(member.getUsername());
            }
            // 如果用手机注册
            if (RegexpUtil.isMatch(member.getUsername(), RegexpCst.VALIDATOR_MOBILE)) {
                details.setMobile(member.getUsername());
            }
            member.setDetails(details);
        }

        // 默认昵称与用户名一致
        if (StringUtil.isBlank(member.getNickName())) {
            member.setNickName(member.getUsername());
        }

        // 初始化用户权限
        List<Role> roles = new ArrayList<>();
        Role defaultRole = roleService.get(DEFAULT_ROLE_CODE);
        if (defaultRole != null) {
            roles.add(defaultRole);
        }
        member.setRoles(roles);
        member.setUserGroups(new ArrayList<UserGroup>());
        //初始化用户状态
        member.setEnabled(true);
        member.setAccountNonLocked(true);
        member.setAccountNonExpired(true);
        member.setCredentialsNonExpired(true);
        // 保存用户
        applicationContext.publishEvent(new RegisterEvent(member = this.memberDao.save(member)));
        return member;
    }

    public MemberDetails update(MemberDetails details) {
        Member member = this.memberDao.get(details.getMemberId());
        BeanUtil.copyProperties(member.getDetails(), details, "memberId");
        this.memberDao.update(member);
        return member.getDetails();
    }

    public List<Member> find(Criterion... criterions) {
        return this.memberDao.find(criterions);
    }

    public Member changePassword(Long id, AuthType type, String oldPassword, String newPassword) {
        Member member = this.memberDao.get(id);
        if (member == null) {
            throw new NotFoundException("用户不存在");
        }
        if (!member.getEnabled()) {
            throw new ValidationException(201.1f, "用户已被禁用");
        }
        switch (type) {
            case password:
                if (!passwordEncoder.matches(member.getPassword(), oldPassword)) {
                    throw new ValidationException(201.2f, "提供的旧密码不正确");
                }
                break;
            case macode:
                break;
        }
        member.setPassword(passwordEncoder.encode(newPassword));
        this.memberDao.update(member);
        return member;
    }

    /**
     * 保存对象
     *
     * @param member member
     * @return Member
     */
    public Member update(Member member, boolean patch) {
        return this.memberDao.save(member, patch);
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
        return this.memberDao.findUniqueBy("username", username);
    }

}
