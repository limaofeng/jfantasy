package org.jfantasy.security.userdetails;

import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.UserGroup;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FantasyUserDetails extends UserDetails {

    List<UserGroup> getUserGroups();

    List<Role> getRoles();

}
