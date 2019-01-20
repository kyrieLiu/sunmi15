package ys.app.pad.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.BR;

/**
 * Created by Administrator on 2017/6/8.
 */

public class VipRechargeInfo extends BaseObservable {


    /**
     * shopId : 1
     * dotime : 1493897377000
     * orderId : 20170504072937235893
     * userId : 100
     * vipUserId : 5
     * vipName : 吕园
     * vipPhone : 18239900618
     * payDate : 1493897383000
     * realAmt : 100
     * vipUserCardNo : 20170401101422790917
     * rechargeAmt : 100
     * payWay : 1001
     * beforeAmt : 0.03
     * afterAmt : 0.04
     * id : 112
     * state : 1
     */

    private String shopId;
    private long dotime;
    private String orderId;
    private int userId;
    private int vipUserId;
    private String vipName;
    private String vipPhone;
    private long payDate;
    private double realAmt;
    private String vipUserCardNo;
    private double rechargeAmt;
    private String payWay;
    private double beforeAmt;
    private double afterAmt;
    private int id;
    private int state;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public long getDotime() {
        return dotime;
    }

    public void setDotime(long dotime) {
        this.dotime = dotime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVipUserId() {
        return vipUserId;
    }

    public void setVipUserId(int vipUserId) {
        this.vipUserId = vipUserId;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getVipPhone() {
        return vipPhone;
    }

    public void setVipPhone(String vipPhone) {
        this.vipPhone = vipPhone;
    }

    @Bindable
    public long getPayDate() {
        return payDate;
    }

    public void setPayDate(long payDate) {
        this.payDate = payDate;
        notifyPropertyChanged(BR.payDate);
    }

    public double getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }

    public String getVipUserCardNo() {
        return vipUserCardNo;
    }

    public void setVipUserCardNo(String vipUserCardNo) {
        this.vipUserCardNo = vipUserCardNo;
    }

    @Bindable
    public double getRechargeAmt() {
        return rechargeAmt;
    }

    public void setRechargeAmt(double rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
        notifyPropertyChanged(BR.rechargeAmt);
    }

    @Bindable
    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
        notifyPropertyChanged(BR.payWay);
    }

    public double getBeforeAmt() {
        return beforeAmt;
    }

    public void setBeforeAmt(double beforeAmt) {
        this.beforeAmt = beforeAmt;
    }

    public double getAfterAmt() {
        return afterAmt;
    }

    public void setAfterAmt(double afterAmt) {
        this.afterAmt = afterAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }
}
