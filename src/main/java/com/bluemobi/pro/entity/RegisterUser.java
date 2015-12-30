package com.bluemobi.pro.entity;

/**
 * 注册用户对象
 * @author yesong
 *
 */
public class RegisterUser extends UserLogin{

	private String code;
	
	private String mobile;
	
	private String nickname;
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
