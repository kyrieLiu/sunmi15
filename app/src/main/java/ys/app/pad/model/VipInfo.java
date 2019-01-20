package ys.app.pad.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaa on 2017/3/24.
 */

public class VipInfo extends BaseSelect implements Serializable{
    /**
     * shopId : 1
     * phone : 18239900618
     * cardNo : 201703241012
     * commodityDiscount : 9
     * productDiscount : 9123
     * productList : ,1,2,3,
     * productNum :
     * commodityList :
     * commodityNum :
     * petNum : 0
     * integral : 0
     * sex : 0
     * birthday : null
     * dotime : 1490337414000
     * money : 0
     * vipTime : null
     * password : 18239900618
     * name : å°A
     * id : 1
     * state : 0
     * type : 12
     * typeName : é»éç±»
     */

    private String shopId;
    private String phone;
    private String cardNo;
    private int cardState;
    private double commodityDiscount;
    private double productDiscount;
    private String productList;
    private String productNum;
    private String productName;
    private String commodityList;
    private String commodityNum;
    private int petNum;
    private int integral;
    private int sex;
    private String birthday;
    private long dotime;
    private double money;
    private long vipTime;
    private String password;
    private String name;
    private long id;
    private int state;
    private int type;
    private String typeName;
    private long requestTime;
    private int isPetBirthdayDiscount;
    private String petBirthdayDiscountName;
    private int petBirthdayDiscountType;
    private int branchId;


    private List<AnimalInfo> petList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

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

    public int getCardState() {
        return cardState;
    }

    public void setCardState(int cardState) {
        this.cardState = cardState;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public long getVipTime() {
        return vipTime;
    }

    public void setVipTime(long vipTime) {
        this.vipTime = vipTime;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public List<AnimalInfo> getPetList() {
        return petList;
    }

    public void setPetList(List<AnimalInfo> petList) {
        this.petList = petList;
    }

    public int getIsPetBirthdayDiscount() {
        return isPetBirthdayDiscount;
    }

    public void setIsPetBirthdayDiscount(int isPetBirthdayDiscount) {
        this.isPetBirthdayDiscount = isPetBirthdayDiscount;
    }

    public String getPetBirthdayDiscountName() {
        return petBirthdayDiscountName;
    }

    public void setPetBirthdayDiscountName(String petBirthdayDiscountName) {
        this.petBirthdayDiscountName = petBirthdayDiscountName;
    }

    public int getPetBirthdayDiscountType() {
        return petBirthdayDiscountType;
    }

    public void setPetBirthdayDiscountType(int petBirthdayDiscountType) {
        this.petBirthdayDiscountType = petBirthdayDiscountType;
    }
}
