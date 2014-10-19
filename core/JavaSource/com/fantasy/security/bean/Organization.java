package com.fantasy.security.bean;

import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fantasy.framework.dao.BaseBusEntity;

/**
 * 组织机构
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-22 下午04:00:57
 * @version 1.0
 */
@JsonIgnoreProperties( { "hibernateLazyInitializer" })
public class Organization extends BaseBusEntity {

	private static final long serialVersionUID = -6159187521342750200L;
	/**
	 * 机构简写
	 */
	private String code;
	/**
	 * 机构名称
	 */
	private String name;
	/**
	 * 机构描述信息
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 上级机构
	 */
	private Organization parentOrganization;

	/**
	 * 下属机构
	 */
	private List<Organization> children;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}

	public Organization getParentOrganization() {
		return parentOrganization;
	}

	public void setParentOrganization(Organization parentOrganization) {
		this.parentOrganization = parentOrganization;
	}

}
