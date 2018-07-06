package com.biz.justin.myFeeNet.entity.userinfo;

import java.io.Serializable;

/**
 * 用户实体类
 */
public class UserInfo implements Serializable {

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
     * 性别 0:女；1:男
     */
    private String sex;
    /**
     * 联系电话
     */
    private String phone;

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

}