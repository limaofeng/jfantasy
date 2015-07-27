package com.fantasy.security.userdetails;

import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.UserGroup;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FantasyUserDetails extends UserDetails {

    List<UserGroup> getUserGroups();

    List<Role> getRoles();

}
