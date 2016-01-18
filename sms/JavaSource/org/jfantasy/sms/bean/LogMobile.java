package org.jfantasy.sms.bean;

import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Entity
@Table(name = "SMS_LOG_MOBILE")
public class LogMobile extends BaseBusEntity {

	private static final long serialVersionUID = -4543185063940657546L;

	@Id
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;
	/**
	 * 发送内容
	 */
	@Column(name = "CONTENT", length = 200)
	private String content;
	/**
	 * 动作描述
	 */
	@Column(name = "description", length = 200)
	private String description;
	/**
	 * 发送状态（true：成功；false：失败）
	 */
	@Column(name = "status")
	private Boolean status;
	/**
	 * 接收者手机号码
	 */
	@Column(name = "mobilephone", length = 20)
	private String mobilephone;

    /**
     * 配置信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFIG_ID",foreignKey = @ForeignKey(name = "FK_CONFIG_LOGMOBILE"))
    private CaptchaConfig captchaConfig;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public CaptchaConfig getCaptchaConfig() {
        return captchaConfig;
    }

    public void setCaptchaConfig(CaptchaConfig captchaConfig) {
        this.captchaConfig = captchaConfig;
    }
}
