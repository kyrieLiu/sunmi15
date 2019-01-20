package ys.app.pad.model;

import java.io.Serializable;

/**
 * Created by aaa on 2017/3/28.
 */
public class ChargeInfo extends BaseSelect implements Serializable{

    /**
     * shopId : 1
     * phone : 18239900618
     * cardNo : 20170324034434293159
     * commodityDiscount : 9
     * productDiscount : 9
     * productList : ,1,2,3,
     * productNum :
     * commodityList :
     * commodityNum :
     * petNum : 4
     * integral : 0
     * sex : 0
     * birthday : null
     * dotime : 1490341495000
     * money : 0
     * vipTime : null
     * orderNo : 20170328013732934677
     * password : 18239900618
     * name : 杨颖
     * id : 2
     * state : 0
     * type : 12
     * typeName : 黄金卡
     */

    private String shopId;
    private String phone;
    private String cardNo;
    private double commodityDiscount;
    private double productDiscount;
    private String productList;
    private String productNum;
    private String commodityList;
    private String commodityNum;
    private int petNum;
    private int integral;
    private int sex;
    private String birthday;
    private long dotime;
    private double money;
    private String vipTime;
    private String orderNo;
    private String password;
    private String name;
    private int id;
    private int state;
    private int type;
    private String typeName;
    private String presentAmt;

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    private String chargeMoney;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public double getCommodityDiscount() {
        return commodityDiscount;
    }

    public void setCommodityDiscount(double commodityDiscount) {
        this.commodityDiscount = commodityDiscount;
    }

    public double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(String commodityList) {
        this.commodityList = commodityList;
    }

    public String getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(String commodityNum) {
        this.commodityNum = commodityNum;
    }

    public int getPetNum() {
        return petNum;
    }

    public void setPetNum(int petNum) {
        this.petNum = petNum;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public long getDotime() {
        return dotime;
    }

    public void setDotime(long dotime) {
        this.dotime = dotime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getVipTime() {
        return vipTime;
    }

    public void setVipTime(String vipTime) {
        this.vipTime = vipTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPresentAmt() {
        return presentAmt;
    }

    public void setPresentAmt(String presentAmt) {
        this.presentAmt = presentAmt;
    }

}
