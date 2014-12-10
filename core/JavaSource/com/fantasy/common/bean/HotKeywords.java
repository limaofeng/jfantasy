package com.fantasy.common.bean;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 热门关键词
 */
@Entity
@Table(name = "HOT_KEYWORDS", uniqueConstraints = { @UniqueConstraint(columnNames = { "TARGET_KEY", "KEYWORDS", "TIME_UNIT", "TIME" }) })
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HotKeywords extends BaseBusEntity {

	private static final long serialVersionUID = 1060015917650774536L;

	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 功能唯一标识
	 */
	@Column(name = "TARGET_KEY", length = 50, nullable = false)
	private String key;
	/**
	 * 关键词
	 */
	@Column(name = "KEYWORDS", length = 150, nullable = false)
	private String keywords;
	/**
	 * 搜索次数
	 */
	@Column(name = "HIT_COUNT", nullable = false)
	private Integer hitCount;
	/**
	 * 时间单位
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TIME_UNIT", length = 8, nullable = true)
	private TimeUnit timeUnit;
	/**
	 * 可能的值<br/>
	 * 天、周、月、年
	 */
	@Column(name = "TIME", length = 8, nullable = true)
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getHitCount() {
		return hitCount;
	}

	public void setHitCount(Integer hitCount) {
		this.hitCount = hitCount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

}
