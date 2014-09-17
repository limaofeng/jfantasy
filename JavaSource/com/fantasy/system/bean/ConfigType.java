package com.fantasy.system.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SYS_CONFIG_TYPE")
public class ConfigType extends BaseBusEntity {

	private static final long serialVersionUID = 4319579291855746985L;

	/**
	 * 代码
	 */
	@Id
	@Column(name = "CODE", length = 20)
	private String code;

	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 200)
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createTime desc")
	@JoinColumn(name = "TYPE",foreignKey = @ForeignKey(name = "FK_SYS_CONFIG_TYPE"))
	private List<Config> configs;

	public ConfigType() {
	}

	public ConfigType(String code) {
		this.code = code;
	}

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

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

}
