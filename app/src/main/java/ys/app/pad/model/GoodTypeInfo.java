package ys.app.pad.model;

import android.databinding.Bindable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import ys.app.pad.BR;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by aaa on 2017/3/21.
 */

@Entity
public class GoodTypeInfo extends BaseSelect implements Serializable {

    /**
     * shopId : 1
     * imgurl : null
     * allcount : null
     * count : 0
     * name : 狗类
     * id : 1
     */


    private String shopId;
    private String imgurl;
    private String allcount;
    private int count;
    private String name;
    @Id(autoincrement = true)
    private long id;
    private int select;
    private long requestTime;
    private double discount;
    private String classifyName;
    private Long vipCardId;
    private int classifyId;
    private int type;
    private int cardId;

    @Generated(hash = 406291466)
    public GoodTypeInfo(String shopId, String imgurl, String allcount, int count,
            String name, long id, int select, long requestTime, double discount,
            String classifyName, Long vipCardId, int classifyId, int type,
            int cardId) {
        this.shopId = shopId;
        this.imgurl = imgurl;
        this.allcount = allcount;
        this.count = count;
        this.name = name;
        this.id = id;
        this.select = select;
        this.requestTime = requestTime;
        this.discount = discount;
        this.classifyName = classifyName;
        this.vipCardId = vipCardId;
        this.classifyId = classifyId;
        this.type = type;
        this.cardId = cardId;
    }

    @Generated(hash = 1036993553)
    public GoodTypeInfo() {
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

    public Long getVipCardId() {
        return this.vipCardId;
    }

    public void setVipCardId(Long vipCardId) {
        this.vipCardId = vipCardId;
    }

    public String getClassifyName() {
        return this.classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
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

    public int getSelect() {
        return this.select;
    }

    public void setSelect(int select) {
        this.select = select;
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

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAllcount() {
        return this.allcount;
    }

    public void setAllcount(String allcount) {
        this.allcount = allcount;
    }

    public String getImgurl() {
        return this.imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }




}
