package com.fantasy.security.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fantasy.security.bean.UserGroup;

@Service("fantasy.auth.UserGroupService")
public class UserGroupService{

	public List<UserGroup> getUserGroupsByResourceId(Long id) {
		return null;
	}
}