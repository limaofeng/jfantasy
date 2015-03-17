package com.fantasy.security.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.service.MailSendService;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.User;
import com.fantasy.security.dao.UserDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("fantasy.auth.UserService")
@Transactional
public class UserService {

    private static final Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private MailSendService mailSendService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;

    /**
     * 保存用户
     *
     * @param user 用户对象
     */
    @CacheEvict(value = {"fantasy.security.userService"}, allEntries = true)
    public void save(User user) {
        if (user.getId() == null) {
            if (StringUtil.isNotBlank(user.getPassword())) {
                user.setPassword(SpringSecurityUtils.getPasswordEncoder().encodePassword(user.getPassword(), null));
            }
            this.userDao.save(user);
        } else {
            if (!"******".equals(user.getPassword())) {
                User u = this.userDao.get(user.getId());
                PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
                if (!encoder.isPasswordValid(u.getPassword(), user.getPassword(), null)) {
                    user.setPassword(encoder.encodePassword(user.getPassword(), null));
                }
            } else {
                user.setPassword(null);// 为NULL时,不会更新字段
            }
            this.userDao.save(user);
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
            Hibernate.initialize(user.getWebsite());
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
    public void login(User user) {
        user.setLastLoginTime(DateUtil.now());
        this.userDao.update(user);
    }

    public void logout(User user) {

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