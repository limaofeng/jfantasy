package com.fantasy.system.ws.dto;

import java.util.List;

public class DataDictionaryTypeDTO {


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

	private List<DataDictionaryDTO> configs;

	public DataDictionaryTypeDTO() {
	}

	public DataDictionaryTypeDTO(String code) {
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

	public List<DataDictionaryDTO> getConfigs() {
		return configs;
	}

	public void setConfigs(List<DataDictionaryDTO> configs) {
		this.configs = configs;
	}

}
