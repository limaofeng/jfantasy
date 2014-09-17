package com.fantasy.system.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * 系统参数配置表
 * 
 * @author
 */
@Entity
@Table(name = "SYS_CONFIG")
@IdClass(ConfigKey.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "parent", "children" })
public class Config extends BaseBusEntity {

	private static final long serialVersionUID = 9059972537363759164L;

	/**
	 * 代码
	 */
	@Id
	private String code;
	/**
	 * 配置类别
	 */
	@Id
	private String type;
	/**
	 * 名称
	 */
	@Column(name = "NAME", length = 200)
	private String name;
	/**
	 * 排序字段
	 */
	@Column(name = "SORT")
	private Integer sort;
	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION", length = 2000)
	private String description;
	/**
	 * 上级编码
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REFRESH })
	@JoinColumns(value = { @JoinColumn(name = "PCODE", referencedColumnName = "CODE"), @JoinColumn(name = "PTYPE", referencedColumnName = "TYPE") },foreignKey =@ForeignKey(name = "FK_SYS_CONFIG_PARENT") )
	private Config parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("sort ASC")
	private List<Config> children;

	public List<Config> getChildren() {
		return children;
	}

	public void setChildren(List<Config> children) {
		this.children = children;
	}

	/**
	 * 设置 代码
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取 代码
	 * 
	 * @return java.lang.String
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * 设置 名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 名称
	 * 
	 * @return java.lang.String
	 */
	public String getName() {
		return this.name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Config getParent() {
		return parent;
	}

	@Transient
	@JsonSerialize(using = ConfigKeySerialize.class)
	public ConfigKey getConfigKey() {
		return ConfigKey.newInstance(this.code, this.type);
	}

	@Transient
	@JsonSerialize(using = ConfigKeySerialize.class)
	public ConfigKey getParentKey() {
		if (this.parent == null) {
			return null;
		}
		return ConfigKey.newInstance(this.parent.code, this.parent.type);
	}

	public void setParent(Config parent) {
		this.parent = parent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getParentName() {
		if (this.getParent() == null)
			return "";
		String parentName = this.getParent().getParentName();
		return parentName + (parentName.equals("") ? "" : ">") + this.getParent().getName();
	}

	public static class ConfigKeySerialize extends JsonSerializer<ConfigKey> {

		@Override
		public void serialize(ConfigKey configKey, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			try {
				if (configKey == null) {
					jgen.writeString("");
				} else {
					jgen.writeString(configKey.getType() + ":" + configKey.getCode());
				}
			} catch (Exception e) {
				jgen.writeString("");
			}
		}

	}

}