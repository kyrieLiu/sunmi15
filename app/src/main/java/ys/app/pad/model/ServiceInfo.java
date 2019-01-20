package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by aaa on 2017/3/30.
 */

public class ServiceInfo extends BaseShoppingCarInfo {

    /**
     * shopId : 1
     * rangeList :
     * realAmt : 150
     * promotionAmt : 0
     * costAmt : 0
     * isPromotion : 0
     * totime : 1490842708000
     * updateTime : null
     * imgpath :
     * unit :
     * orderInfo :
     * name : é å
     * id : 4
     * type : 1
     * typeName : å¤§ç
     */

    private String shopId;
    private int isFold;
    private String rangeList;
    private double realAmt;
    private double promotionAmt;
    private double costAmt;
    private int isPromotion;
    private long totime;
    private String updateTime;
    private String imgpath;
    private String unit;
    private String info;
    private String name;
    private int id;
    private int type;
    private String typeName;
    private int promotionType;
    private double promotionNum;
    private String sortLetters; // 索引字母
    private long beginTime;
    private long endTime;
    private double vipAmt;

    public int getIsFold() {
        return isFold;
    }

    public void setIsFold(int isFold) {
        this.isFold = isFold;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public double getPromotionNum() {
        return promotionNum;
    }

    public void setPromotionNum(double promotionNum) {
        this.promotionNum = promotionNum;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getRangeList() {
        return rangeList;
    }

    public void setRangeList(String rangeList) {
        this.rangeList = rangeList;
    }
    @Bindable
    public double getVipAmt() {
        return vipAmt;
    }

    public void setVipAmt(double vipAmt) {
        this.vipAmt = vipAmt;
        notifyPropertyChanged(BR.vipAmt);
    }

    @Bindable
    public double getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
        notifyPropertyChanged(BR.realAmt);
    }

    public double getPromotionAmt() {
        return promotionAmt;
    }

    public void setPromotionAmt(double promotionAmt) {
        this.promotionAmt = promotionAmt;
    }

    @Bindable
    public double getCostAmt() {
        return costAmt;
    }

    public void setCostAmt(double costAmt) {
        this.costAmt = costAmt;
        notifyPropertyChanged(ys.app.pad.BR.costAmt);
    }

    @Bindable
    public int getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
        notifyPropertyChanged(ys.app.pad.BR.isPromotion);
    }

    public long getTotime() {
        return totime;
    }

    public void setTotime(long totime) {
        this.totime = totime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Bindable
    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
        notifyPropertyChanged(BR.imgpath);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        notifyPropertyChanged(BR.typeName);
    }
    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
