package org.jfantasy.framework.util.json.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.Sets;

import java.util.Date;
import java.util.Set;

@JsonFilter("myFilter")
public class User {
	private String name;
	private Date createDate;
	private Set<Article> articles = Sets.newHashSet();
	private String username;
	private boolean enabled;
	private String nickName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonManagedReference
//	@JsonIgnore
	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}
}
