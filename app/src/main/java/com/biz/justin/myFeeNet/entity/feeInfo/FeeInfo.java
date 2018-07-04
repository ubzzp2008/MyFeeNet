package com.biz.justin.myFeeNet.entity.feeInfo;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version V0.1
 * @项目名称：myproject
 * @类名称：FeeInfo
 * @类描述：
 * @创建人：justin
 * @创建时间：2018-06-29 21:32
 */
public class FeeInfo implements Serializable {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 费用类型 收入：IN，支出：OUT
     */
    private String feeType;//费用类型

    /**
     * 费用日期 yyyy-MM-dd
     */
    private String useDate;//费用日期

    /**
     * 费用金额
     */
    private BigDecimal money;//费用金额

    /**
     * 费用说明
     */
    private String useContent;//费用说明

    /**
     * 费用备注
     */
    private String note;//备注
    /**
     * 费用备注
     */
    private String addUser;//备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getUseContent() {
        return useContent;
    }

    public void setUseContent(String useContent) {
        this.useContent = useContent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }
}
