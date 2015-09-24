package com.fantasy.framework.util.json.bean;


import com.fasterxml.jackson.annotation.JsonBackReference;

public class Article {
	private String title;
	
	private User user;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonBackReference
//	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
