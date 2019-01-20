package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;


/**
 * Created by aaa on 2017/3/21.
 */
public class GoodInfo extends BaseShoppingCarInfo implements Serializable {

    /**
     * shopId : 1
     * realAmt : 10
     * promotionAmt : 0
     * costAmt : 0
     * stockNum : 0
     * spec :
     * barCode :
     * manufactureTime : null
     * expiryDate : 0
     * imgpath :
     * unit :
     * label :
     * brandId : 0
     * brandName :
     * isPromotion : 0
     * totime : null
     * updateTime : null
     * attribute :
     * orderInfo :
     * name : 旺旺旺
     * id : 1
     * type : 1
     * typeName :
     */

    private String shopId;
    private int isFold;
    private double realAmt;
    private double promotionAmt;
    private double costAmt;
    private int stockNum;
    private String spec;
    private String barCode;
    private long manufactureTime;
    private int expiryDate;
    private String imgpath;
    private String unit;
    private String label;
    private int brandId;
    private String brandName;
    private int isPromotion;
    private long totime;
    private long updateTime;
    private String attribute;
    private String info;
    private String name;
    private int inventoryNum;
    private long id;
    private int type;
    private String typeName;
    private int promotionType;
    private double promotionNum;
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


    @Bindable
    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        notifyPropertyChanged(BR.typeName);
    }

    @Bindable
    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public int getInventoryNum() {
        return this.inventoryNum;
    }

    public void setInventoryNum(int inventoryNum) {
        this.inventoryNum = inventoryNum;
        notifyPropertyChanged(BR.inventoryNum);
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getTotime() {
        return this.totime;
    }

    public void setTotime(long totime) {
        this.totime = totime;
    }

    @Bindable
    public int getIsPromotion() {
        return this.isPromotion;
    }

    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
        notifyPropertyChanged(BR.isPromotion);
    }

    public String getBrandName() {
        return this.brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getBrandId() {
        return this.brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Bindable
    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        notifyPropertyChanged(BR.unit);
    }

    public String getImgpath() {
        return this.imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(int expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getManufactureTime() {
        return this.manufactureTime;
    }

    public void setManufactureTime(long manufactureTime) {
        this.manufactureTime = manufactureTime;
    }

    @Bindable
    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
        notifyPropertyChanged(BR.barCode);
    }

    public String getSpec() {
        return this.spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Bindable
    public int getStockNum() {
        return this.stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
        notifyPropertyChanged(BR.stockNum);
    }

    @Bindable
    public double getCostAmt() {
        return this.costAmt;
    }

    public void setCostAmt(double costAmt) {
        this.costAmt = costAmt;
        notifyPropertyChanged(BR.costAmt);
    }

    public double getPromotionAmt() {
        return this.promotionAmt;
    }

    public void setPromotionAmt(double promotionAmt) {
        this.promotionAmt = promotionAmt;
    }

    @Bindable
    public double getRealAmt() {
        return this.realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
        notifyPropertyChanged(BR.realAmt);
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    @Bindable
    public double getVipAmt() {
        return vipAmt;
    }

    public void setVipAmt(double vipAmt) {
        this.vipAmt = vipAmt;
        notifyPropertyChanged(BR.vipAmt);
    }

    @Override
    public boolean equals(Object obj) {
        GoodInfo info= (GoodInfo) obj;
        return id==info.getId();
    }
}
