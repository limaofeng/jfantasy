package com.fantasy.sms.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 短信验证码
 * 
 * @author 李茂峰
 * @since 2013-7-4 下午01:56:29
 * @version 1.0
 */
@Entity
@Table(name = "SMS_CAPTCHA")
public class Captcha extends BaseBusEntity {

	private static final long serialVersionUID = -5584016913883386904L;

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, length = 32)
	private String id;
	/**
	 * 配置信息
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFIG_ID",foreignKey = @ForeignKey(name = "FK_CONFIG_CAPTCHA"))
	private CaptchaConfig captchaConfig;
	/**
	 * 绑定的会话ID
	 */
	@Column(name = "SESSION_ID", length = 120)
	private String sessionId;
	/**
	 * 生成的CODE值
	 */
	@Column(name = "VALUE", length = 120)
	private String value;
	/**
	 * 已经重试的次数
	 */
	@Column(name = "RETRY")
	private int retry;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

    public CaptchaConfig getCaptchaConfig() {
        return captchaConfig;
    }

    public void setCaptchaConfig(CaptchaConfig captchaConfig) {
        this.captchaConfig = captchaConfig;
    }
}
