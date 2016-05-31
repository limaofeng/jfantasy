package org.jfantasy.security.service;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.LoginException;
import org.jfantasy.framework.spring.mvc.error.PasswordException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.context.LoginEvent;
import org.jfantasy.security.context.LogoutEvent;
import org.jfantasy.security.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

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
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return this.userDao.save(user);
        } else {
            if (!"******".equals(user.getPassword())) {
                User u = this.userDao.get(user.getId());
                if (!passwordEncoder.matches(u.getPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        return this.userDao.findUniqueBy("username", username);
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
        if (user == null || !passwordEncoder.matches(user.getPassword(), password)) {
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

}