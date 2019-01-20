package ys.app.pad.model;

import android.databinding.Bindable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import ys.app.pad.BR;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by aaa on 2017/3/22.
 */
@Entity
public class ServiceTypeInfo extends BaseShoppingCarInfo {


    /**
     * allcount : null
     * imgurl :
     * shopId : 1
     * cardType : 0
     * count : null
     * name : 大狗
     * id : 1
     */

    private String allcount;
    private String imgurl;
    private String shopId;
    private int cardType;
    private String count;
    private String name;
    @Id(autoincrement = true)
    private long id;
    private long requestTime;
    private double discount;
    private Long vipCardId;
    private String classifyName;
    private int classifyId;
    private int type;
    private int cardId;
    private int select;
    @Generated(hash = 719197445)
    public ServiceTypeInfo(String allcount, String imgurl, String shopId,
            int cardType, String count, String name, long id, long requestTime,
            double discount, Long vipCardId, String classifyName, int classifyId,
            int type, int cardId, int select) {
        this.allcount = allcount;
        this.imgurl = imgurl;
        this.shopId = shopId;
        this.cardType = cardType;
        this.count = count;
        this.name = name;
        this.id = id;
        this.requestTime = requestTime;
        this.discount = discount;
        this.vipCardId = vipCardId;
        this.classifyName = classifyName;
        this.classifyId = classifyId;
        this.type = type;
        this.cardId = cardId;
        this.select = select;
    }
    @Generated(hash = 1742879372)
    public ServiceTypeInfo() {
    }
    public int getSelect() {
        return this.select;
    }
    public void setSelect(int select) {
        this.select = select;
    }
    public int getCardId() {
        return this.cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getClassifyId() {
        return this.classifyId;
    }
    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }
    public String getClassifyName() {
        return this.classifyName;
    }
    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
    public Long getVipCardId() {
        return this.vipCardId;
    }
    public void setVipCardId(Long vipCardId) {
        this.vipCardId = vipCardId;
    }
    @Bindable
    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        notifyPropertyChanged(BR.discount);
    }
    public long getRequestTime() {
        return this.requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCount() {
        return this.count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public int getCardType() {
        return this.cardType;
    }
    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
    public String getImgurl() {
        return this.imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public String getAllcount() {
        return this.allcount;
    }
    public void setAllcount(String allcount) {
        this.allcount = allcount;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }



}
