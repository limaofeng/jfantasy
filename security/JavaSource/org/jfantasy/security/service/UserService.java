package org.jfantasy.security.service;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.service.MailSendService;
import org.jfantasy.framework.spring.mvc.error.LoginException;
import org.jfantasy.framework.spring.mvc.error.PasswordException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.security.SpringSecurityUtils;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.context.LoginEvent;
import org.jfantasy.security.context.LogoutEvent;
import org.jfantasy.security.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private MailSendService mailSendService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 保存用户
     *
     * @param user 用户对象
     */
    @CacheEvict(value = {"fantasy.security.userService"}, allEntries = true)
    public User save(User user) {
        if (user.getId() == null) {
            if (StringUtil.isNotBlank(user.getPassword())) {
                user.setPassword(SpringSecurityUtils.getPasswordEncoder().encodePassword(user.getPassword(), null));
            }
            return this.userDao.save(user);
        } else {
            if (!"******".equals(user.getPassword())) {
                User u = this.userDao.get(user.getId());
                if (!passwordEncoder.isPasswordValid(u.getPassword(), user.getPassword(), null)) {
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                }
            } else {
                user.setPassword(null);// 为NULL时,不会更新字段
            }
            return this.userDao.save(user);
        }
    }

    public boolean usernameNotExists(String username, Long id) {
        User user = this.userDao.findUniqueBy("username", username);
        return (user == null) || (user.getId().equals(id));
    }

    @Cacheable(value = "fantasy.security.userService", key = "'findUniqueByUsername' + #username ")
    public User findUniqueByUsername(String username) {
        User user = this.userDao.findUniqueBy("username", username);
        if (user != null) {
            SpringSecurityUtils.getAuthorities(user);
        }
        return user;
    }

    public Pager<User> findPager(Pager<User> pager, List<PropertyFilter> filters) {
        return this.userDao.findPager(pager, filters);
    }

    @CacheEvict(value = {"fantasy.security.userService"}, allEntries = true)
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.userDao.delete(id);
        }
    }

    @Cacheable(value = "fantasy.security.userService", key = "'get' + #id")
    public User get(Long id) {
        return this.userDao.get(id);
    }

    @Cacheable(value = "fantasy.security.userService", key = "'findUserByRoleCode' + #q + #roleCode")
    public List<User> findUserByRoleCode(String q, String roleCode) {
        if (StringUtil.isNotBlank(roleCode)) {
            return this.userDao.find(new Criterion[]{Restrictions.eq("roles.code", roleCode), Restrictions.like("details.name", "%" + q + "%")}, 0, 20);
        } else {
            return this.userDao.find(new Criterion[]{Restrictions.like("details.name", "%" + q + "%")}, 0, 20);
        }
    }

    @CacheEvict(value = {"fantasy.security.userService"}, allEntries = true)
    public User login(String username, String password) {
        User user = this.userDao.findUniqueBy("username", username);
        if (user == null || !passwordEncoder.isPasswordValid(user.getPassword(), password, null)) {
            throw new PasswordException("用户名和密码错误");
        }
        if (!user.isEnabled()) {
            throw new LoginException("用户被禁用");
        }
        if (!user.isAccountNonLocked()) {
            throw new LoginException("用户被锁定");
        }
        user.setLastLoginTime(DateUtil.now());
        this.userDao.save(user);
        this.applicationContext.publishEvent(new LoginEvent(user));
        return user;
    }

    public void logout(String username) {
        this.applicationContext.publishEvent(new LogoutEvent(findUniqueByUsername(username)));
    }

    public String retrievePassword(String email) {
        String massige = "";
        User user = this.userDao.findUnique(Restrictions.eq("details.email", email));
        if (user != null) {
            mailSendService.sendHtmlEmail("系统密码找回", "您的的密码为" + user.getPassword(), email);
            massige = "你的密码已发送至你填写的邮箱,请注意查收";
        } else {
            massige = "预留邮箱地址不正确,请与管理员联系";
        }
        return massige;
    }

}