package com.fantasy.security.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fantasy.framework.dao.BaseBusEntity;

/**
 * 岗位
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-22 下午04:00:48
 * @version 1.0
 */
@Entity
@Table(name = "AUTH_JOB")
@JsonIgnoreProperties( { "hibernateLazyInitializer" })
public class Job extends BaseBusEntity {

	private static final long serialVersionUID = -7020427994563623645L;

	/**
	 * 岗位简写
	 */
	@Id
	@Column(name = "CODE")
	private String code;
	/**
	 * 岗位名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 岗位描述信息
	 */
	@Column(name = "DESCRIPTION")
	private String description;
	/**
	 * 所属组织机构
	 */
	@Transient
	private List<Organization> organizations;

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

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

}
