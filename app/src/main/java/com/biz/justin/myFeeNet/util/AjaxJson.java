package com.biz.justin.myFeeNet.util;

import java.io.Serializable;

public class AjaxJson implements Serializable {
    private boolean success = false;// 是否成功
    private String msg = "";// 提示信息
    private String obj = "";// 其他信息

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}
