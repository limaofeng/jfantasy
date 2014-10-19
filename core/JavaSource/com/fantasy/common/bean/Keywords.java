package com.fantasy.common.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fantasy.framework.dao.BaseBusEntity;

/**
 * 自定义关键词表(相当于词库)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-23 下午3:25:46
 * @version 1.0
 */
@Entity
@Table(name = "KEYWORDS")
public class Keywords extends BaseBusEntity {

	public enum Type {
		ext, stop;
	}

	private static final long serialVersionUID = 3340627269786275436L;

	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 类型
	 */
	@Column(name = "TYPE", nullable = true)
	private Type type;
	/**
	 * 词组
	 */
	@Column(name = "WORDS", nullable = true)
	private String words;

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
