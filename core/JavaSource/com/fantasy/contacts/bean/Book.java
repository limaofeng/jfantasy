package com.fantasy.contacts.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址薄(Address Book)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-15 上午11:53:44
 * @version 1.0
 */
@Entity
@Table(name = "CONTACTS_BOOK")
@JsonIgnoreProperties( { "hibernateLazyInitializer", "handler"})
public class Book extends BaseBusEntity {

	private static final long serialVersionUID = -7969549623026789527L;

	public Book() {
		super();
	}
	
	public Book(Long id) {
		super();
		this.id = id;
	}

	public Book(String username) {
		super();
		this.owner = username;
		this.ownerType = "1";
	}

	public Book(String owner, String ownerType) {
		super();
		this.owner = owner;
		this.ownerType = ownerType;
		this.groups = new ArrayList<Group>();
		this.linkmans = new ArrayList<Linkman>();
	}

	@Id
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;

	@Column(name = "NAME", length = 20)
	private String name;
	/**
	 * 所有者
	 */
	@Column(name = "OWNER", length = 20)
	private String owner;

	/**
	 * 所有者类型<br/>
	 * 1.个人<br/>
	 * 2.部门<br/>
	 * 3.角色<br/>
	 * 4.联系薄<br/>
	 */
	@Column(name = "OWNER_TYPE", length = 20)
	private String ownerType;
	/**
	 * 所有联系人
	 */
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	@OrderBy("createTime DESC")
	private List<Linkman> linkmans;
	/**
	 * 联系人分组列表
	 */
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	@OrderBy("createTime ASC")
	private List<Group> groups;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Linkman> getLinkmans() {
		return linkmans;
	}

	public void setLinkmans(List<Linkman> linkmans) {
		this.linkmans = linkmans;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || this.getId() == null || !(obj instanceof Book) || ((Book) obj).getId() == null){
            return super.equals(obj);
        }
		return this.getId().equals(((Book) obj).getId());
	}

	@Override
	public int hashCode() {
		return this.getId() != null ? this.getId().hashCode() : super.hashCode();
	}

}
