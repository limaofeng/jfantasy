package com.fantasy.contacts.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 联系人群组
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-15 上午11:29:20
 * @version 1.0
 */
@Entity
@Table(name = "CONTACTS_GROUP")
@JsonIgnoreProperties( { "hibernateLazyInitializer", "book", "linkmans" })
public class Group extends BaseBusEntity {

	private static final long serialVersionUID = -5646654443086634257L;

	@Id
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;

	@Column(name = "NAME", length = 20)
	private String name;

	/**
	 * 所属通讯录
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REFRESH })
	@JoinColumn(name = "BOOK_ID")
	private Book book;

	@ManyToMany(targetEntity = Linkman.class, fetch = FetchType.LAZY)
	@JoinTable(name = "CONTACTS_GROUP_LINKMAN", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "LINKMAN_ID"))
	private List<Linkman> linkmans;

	@Column(name = "DESCRIPTION", length = 2000)
	private String description;

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

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<Linkman> getLinkmans() {
		return linkmans;
	}

	public void setLinkmans(List<Linkman> linkmans) {
		this.linkmans = linkmans;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Group) {
			return this.id.equals(((Group) obj).getId());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return ObjectUtil.isNotNull(id) ? id.hashCode() : super.hashCode();
	}

}
