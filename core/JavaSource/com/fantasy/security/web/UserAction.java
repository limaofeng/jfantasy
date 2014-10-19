package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import com.fantasy.security.userdetails.AdminUser;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class UserAction extends ActionSupport {

	private static final long serialVersionUID = 4738280714802564952L;

	@Resource(name = "fantasy.auth.UserService")
	private transient UserService userService;

	/**
	 * 用户列表
	 * 
	 * @功能描述
	 * @return
	 */
	public String index() {
		this.search(new Pager<User>(), new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 用户查询
	 * 
	 * @功能描述
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<User> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, this.userService.findPager(pager, filters));
		return JSONDATA;
	}

	public String save(User user) {
		this.userService.save(user);
		this.attrs.put(ROOT, user);
		return JSONDATA;
	}

	public String edit(Long id) {
		this.attrs.put("user", this.userService.get(id));
		return SUCCESS;
	}

	public String resetpwd(String oldPwd, String newPwd) {
		AdminUser simpleUser = SpringSecurityUtils.getCurrentUser(AdminUser.class);
		if (!simpleUser.getPassword().equals(oldPwd)) {
			this.addActionError("旧密码不正确，请重新输入!");
			return JSONDATA;
		}
		User newUser = new User();
		User user = simpleUser.getUser();
		newUser.setId(user.getId());
		newUser.setPassword(newPwd);
		userService.save(newUser);
		user.setPassword(newPwd);
		return JSONDATA;
	}

	/**
	 * 删除用户
	 * 
	 * @功能描述
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public String delete(Long... ids) {
		this.userService.delete(ids);
		return JSONDATA;
	}

	public String autocomplete(String q, String roleCode) {
		this.attrs.put(ROOT, userService.findUserByRoleCode(q, roleCode));
		return JSONDATA;
	}

    public String profile(){
        this.attrs.put("user",userService.findUniqueByUsername(SpringSecurityUtils.getCurrentUserName()));
        return SUCCESS;
    }

}
