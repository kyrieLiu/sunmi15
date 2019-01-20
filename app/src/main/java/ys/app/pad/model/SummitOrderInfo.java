package ys.app.pad.model;

import java.io.Serializable;

/**
 * 作者：lv
 * 时间：2017/4/6 21:33
 */

public class SummitOrderInfo implements Serializable{


    /**
     * shopId : 1
     * vipUserId : 2
     * orderId : 20170406093047739412
     * shopName :
     * countMoney : 20
     * createDate : 1491485447000
     * productId : 1
     * productType : 1
     * productTypeName :
     * price : 10
     * vipPrice : 0.9
     * img : /Test/images/rectangle_blue.png
     * countVipMoney : 1.8
     * unit :
     * userId : 100
     * count : 2
     * info :
     * name : 狗粮A
     * id : 42
     * state : 0
     * type : 1
     */

    private String shopId;
    private int vipUserId;
    private String orderId;
    private String shopName;
    private double countMoney;
    private long createDate;
    private int productId;
    private int productType;
    private String productTypeName;
    private double price;
    private double vipPrice;
    private String img;
    private double countVipMoney;
    private String unit;
    private int userId;
    private int count;
    private String info;
    private String name;
    private int id;
    private int state;
    private int type;
    private int isPromotion;
    private int promotionType;
    private int isFold;
    private double countPromotionMoney;
    private double promotionPrice;
    private int isClassification;
    private double realMoney;
    private String vipUserName;
    private String vipUserPhone;

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    private int isGift;

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getIsFold() {
        return isFold;
    }

    public void setIsFold(int isFold) {
        this.isFold = isFold;
    }

    public double getCountPromotionMoney() {
        return countPromotionMoney;
    }

    public void setCountPromotionMoney(double countPromotionMoney) {
        this.countPromotionMoney = countPromotionMoney;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getVipUserId() {
        return vipUserId;
    }

    public void setVipUserId(int vipUserId) {
        this.vipUserId = vipUserId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(double countMoney) {
        this.countMoney = countMoney;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getCountVipMoney() {
        return countVipMoney;
    }

    public void setCountVipMoney(double countVipMoney) {
        this.countVipMoney = countVipMoney;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public int getIsClassification() {
        return isClassification;
    }

    public void setIsClassification(int isClassification) {
        this.isClassification = isClassification;
    }

    public String getVipUserName() {
        return vipUserName;
    }

    public void setVipUserName(String vipUserName) {
        this.vipUserName = vipUserName;
    }

    public String getVipUserPhone() {
        return vipUserPhone;
    }

    public void setVipUserPhone(String vipUserPhone) {
        this.vipUserPhone = vipUserPhone;
    }
}
