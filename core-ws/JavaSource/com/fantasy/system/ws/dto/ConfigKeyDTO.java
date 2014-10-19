package com.fantasy.system.ws.dto;

import java.io.Serializable;

public class ConfigKeyDTO implements Serializable {

	private static final long serialVersionUID = 6300479353590087147L;

	public static ConfigKeyDTO newInstance(String code, String type) {
		return new ConfigKeyDTO(code, type);
	}

	/**
	 * 代码
	 */
	private String code;

	/**
	 * 配置类别
	 */
	private String type;

	public ConfigKeyDTO() {
	}

	public ConfigKeyDTO(String code, String type) {
		super();
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type + ":" + this.code;
	}

}
