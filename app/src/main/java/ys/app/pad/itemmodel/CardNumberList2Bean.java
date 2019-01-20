package ys.app.pad.itemmodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by liuyin on 2017/12/13.
 */
@Entity
public class CardNumberList2Bean implements Serializable{
    /**
     * cardId : 170
     * id : 1
     * number : 2
     * productId : 476
     * productName : 剪毛
     * realAmt : 0
     * type : 2
     */

    private int cardId;
    @Id(autoincrement = true)
    private long id;
    private int number;
    private int productId;
    private String productName;
    private double realAmt;
    private int type;
    private Long numCardId;
    public Long getNumCardId() {
        return this.numCardId;
    }
    public void setNumCardId(Long numCardId) {
        this.numCardId = numCardId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public double getRealAmt() {
        return this.realAmt;
    }
    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getProductId() {
        return this.productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getCardId() {
        return this.cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    @Generated(hash = 2060360707)
    public CardNumberList2Bean(int cardId, long id, int number, int productId,
            String productName, double realAmt, int type, Long numCardId) {
        this.cardId = cardId;
        this.id = id;
        this.number = number;
        this.productId = productId;
        this.productName = productName;
        this.realAmt = realAmt;
        this.type = type;
        this.numCardId = numCardId;
    }
    @Generated(hash = 1998091456)
    public CardNumberList2Bean() {
    }

    @Override
    public boolean equals(Object obj) {

        return super.equals(obj);
    }
}
