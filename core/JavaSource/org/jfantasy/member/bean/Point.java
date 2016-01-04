package org.jfantasy.member.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MEM_POINTS")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
public class Point extends BaseBusEntity {

	private static final long serialVersionUID = -2242726270536854841L;
	
	public enum Status {
		/**
		 * 收入(分)
		 */
		add("收入"),
		/**
		 * 支出(分)
		 */
		pay("支出");

		private String value;

		private Status(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;

	/**
	 * 备注
	 */
	@Column(name = "DESCRIPTION")
	private String description;
	/**
	 * 状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20, nullable = false)
	private Status status;
	/**
	 * 分数
	 */
	@Column(name = "POINT", length = 10, scale = 2,nullable = false)
	private Integer score;
	/**
	 * 会员
	 */
	@ManyToOne
	@JoinColumn(name = "MEMBER", nullable = false)
	private Member member;
	/**
	 * 标题
	 */
	@Column(name = "TITLE", nullable = false)
	private String title;
	/**
	 * 来源/用途 url
	 */
	@Column(name = "URL", length = 1024)
	private String url;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
