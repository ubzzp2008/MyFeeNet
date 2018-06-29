package com.biz.justin.myFeeNet.entity;

import java.io.Serializable;

/**
 * 用户实体类
 */
public class UserInfo implements Serializable{

	/**
	 * 登陆账号
	 */
	private String userName;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 登陆密码
	 */
	private String password;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 组织机构ID
	 */
	private String sapvendorId;
	/**
	 * 组织机构名称
	 */
	private String sapvendorName;

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSapvendorId() {
		return sapvendorId;
	}

	public void setSapvendorId(String sapvendorId) {
		this.sapvendorId = sapvendorId;
	}

	public String getSapvendorName() {
		return sapvendorName;
	}

	public void setSapvendorName(String sapvendorName) {
		this.sapvendorName = sapvendorName;
	}

}