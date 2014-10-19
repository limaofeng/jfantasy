package com.fantasy.member.ws.dto;

import java.io.Serializable;
import java.util.Date;

public class MemberDetailsDTO implements Serializable {

	private static final long serialVersionUID = -2371459979188627292L;

	private Long memberId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 移动电话
	 */
	private String mobile;
	/**
	 * 固定电话
	 */
	private String tel;
	/**
	 * E-mail
	 */
	private String email;

	private Boolean mailValid;

	private Boolean mobileValid;

	/**
	 * 网址
	 */
	private String website;
	/**
	 * 描述信息
	 */
	private String description;

	/**
	 * 是否为vip用户
	 */
	private Boolean vip;
	/**
	 * 用户积分
	 */
	private Integer score;

	

	/**
	 * 发票抬头
	 */
	private String invoice;
	
	/**
	 * 账单邮寄地址
	 * 
	 */
	private String checkaddress;
	

	/**
	 * qq
	 * 
	 */
	private String qq;

	/**
	 * 身份证号
	 * 
	 */
	
	private String cardid;
	
	/**
	 * 住址
	 * 
	 */
	private String address;
	
	
	
	/**
	 * 头像
	 * 
	 */
	private String portrait;
	
	
	
	
	
	
	
	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getMailValid() {
		return mailValid;
	}

	public void setMailValid(Boolean mailValid) {
		this.mailValid = mailValid;
	}

	public Boolean getMobileValid() {
		return mobileValid;
	}

	public void setMobileValid(Boolean mobileValid) {
		this.mobileValid = mobileValid;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getCheckaddress() {
		return checkaddress;
	}

	public void setCheckaddress(String checkaddress) {
		this.checkaddress = checkaddress;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
