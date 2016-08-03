package org.jfantasy.mall.stock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 预警设置
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-26 上午11:39:53
 * @version 1.0
 */
@Entity
@Table(name = "MALL_WARNING_SETTINGS")
@JsonIgnoreProperties({ "hibernate_lazy_initializer" })
public class WarningSettings extends BaseBusEntity {

	private static final long serialVersionUID = 5481431980082539446L;

	@Id
	@Column(name = "ID", insertable = true, updatable = false)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	
	/**
	 * 判断是否超出警戒线
	 */
	@Column(name = "EXPRESSION", nullable = false)
	private String expression;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
