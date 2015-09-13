package com.fantasy.sms.bean;

import com.fantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 短信验证码定制方案
 * 
 * @author 李茂峰
 * @since 2013-7-4 上午11:24:41
 * @version 1.0
 */
@Entity
@Table(name = "SMS_CAPTCHA_CONFIG")
public class CaptchaConfig extends BaseBusEntity {

	private static final long serialVersionUID = -124846568914185074L;

	@Id
	@Column(name = "ID", nullable = false,updatable = false, length = 32)
	private String id;
    /**
     * 服务器名称
     */
    @Column(name = "SERVER_NAME", length = 50)
    private String serverName;

	/**
	 * 生成验证码的长度
	 */
	@Column(name = "WORD_LENGTH", length = 2)
	private int wordLength = 6;
	/**
	 * 验证码随机字符串
	 */
	@Column(name = "RANDOM_WORD", length = 50)
	private String randomWord = "0123456789";
	/**
	 * 验证码验证失效时间
	 */
	@Column(name = "EXPIRES", length = 8)
	private int expires = 24 * 60 * 60 * 60;
	/**
	 * 验证码重复生成时间
	 */
	@Column(name = "ACTIVE", length = 8)
	private int active = 5 * 60;
	/**
	 * 验证码验证重试次数
	 */
	@Column(name = "RETRY", length = 8)
	private int retry = 3;
	/**
	 * 模板路径
	 */
	@Column(name = "TEMPLATE", length = 50)
	private String template;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getRandomWord() {
		return randomWord;
	}

	public void setRandomWord(String randomWord) {
		this.randomWord = randomWord;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
