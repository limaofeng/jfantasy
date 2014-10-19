package com.fantasy.system.ws.dto;

import java.util.List;

public class ConfigTypeDTO {


	/**
	 * 代码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	private List<ConfigDTO> configs;

	public ConfigTypeDTO() {
	}

	public ConfigTypeDTO(String code) {
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

	public List<ConfigDTO> getConfigs() {
		return configs;
	}

	public void setConfigs(List<ConfigDTO> configs) {
		this.configs = configs;
	}

}
