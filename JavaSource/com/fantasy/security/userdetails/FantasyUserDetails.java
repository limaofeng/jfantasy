package com.fantasy.security.userdetails;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.UserGroup;

public interface FantasyUserDetails extends UserDetails{

	List<UserGroup> getUserGroups();

	List<Role> getRoles();

}
