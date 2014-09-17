package com.fantasy.contacts.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 自定义字段
 * 
 * @author 李茂峰
 * @since 2013-3-15 上午11:18:06
 * @version 1.0
 */
public class LinkmanAttribute extends BaseBusEntity {

	private static final long serialVersionUID = 3862476075020180077L;

	@Id
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 所有者类型<br/>
	 * 1.全部<br/>
	 * 2.个人<br/>
	 * 3.部门<br/>
	 * 4.角色<br/>
	 * 5.联系薄<br/>
	 */
	private String ownerType;
	/**
	 * 所有者
	 */
	private String owner;
	/**
	 * 字段应用位置<br/>
	 * 1.联系人<br/>
	 * 2.地址<br/>
	 * 3.邮箱<br/>
	 * 4.电话<br/>
	 */
	private String position;
	/**
	 * 字段名称
	 */
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
