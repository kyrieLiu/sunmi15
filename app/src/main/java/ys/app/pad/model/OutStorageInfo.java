package ys.app.pad.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class OutStorageInfo implements Serializable{


    /**
     * headOfficeId : null
     * branchId : null
     * commdityName : 套餐三（POS+收银箱）
     * commodityId : null
     * reasons :
     * num : 1000
     * costAmt : 9876
     * realAmt : 2880
     * dotime : 1503049057000
     * shopId : null
     * unit : 台
     * userId : null
     * id : 1590
     * type : null
     * typeName : 主粮系列
     * realMoney : 交易价
     */

    private Object headOfficeId;
    private Object branchId;
    private String commdityName;
    private Object commodityId;
    private String reasons;
    private int num;
    private double costAmt;
    private double realAmt;
    private long dotime;
    private Object shopId;
    private String unit;
    private Object userId;
    private int id;
    private Object type;
    private String typeName;
    private double realMoney;

    public Object getHeadOfficeId() {
        return headOfficeId;
    }

    public void setHeadOfficeId(Object headOfficeId) {
        this.headOfficeId = headOfficeId;
    }

    public Object getBranchId() {
        return branchId;
    }

    public void setBranchId(Object branchId) {
        this.branchId = branchId;
    }

    public String getCommdityName() {
        return commdityName;
    }

    public void setCommdityName(String commdityName) {
        this.commdityName = commdityName;
    }

    public Object getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Object commodityId) {
        this.commodityId = commodityId;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getCostAmt() {
        return costAmt;
    }

    public void setCostAmt(double costAmt) {
        this.costAmt = costAmt;
    }

    public double getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }

    public long getDotime() {
        return dotime;
    }

    public void setDotime(long dotime) {
        this.dotime = dotime;
    }

    public Object getShopId() {
        return shopId;
    }

    public void setShopId(Object shopId) {
        this.shopId = shopId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }
}
