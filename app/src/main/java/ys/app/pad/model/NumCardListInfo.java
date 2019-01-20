package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.greendao.CardNumberList2BeanDao;
import com.greendao.DaoSession;
import com.greendao.NumCardListInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

import ys.app.pad.itemmodel.CardNumberList2Bean;

/**
 * Created by liuyin on 2017/12/13.
 */
@Entity
public class NumCardListInfo extends BaseSelect implements Serializable{
    /**
     * branchId : 206241
     * cardNumberList2 : [{"cardId":170,"id":1,"number":2,"productId":476,"productName":"剪毛","realAmt":0,"type":2},{"cardId":170,"id":2,"number":1,"productId":401,"productName":"大型犬测试","realAmt":0,"type":2}]
     * classification : 1
     * commodityList :
     * commodityNum :
     * expiryDate : 0
     * headOfficeId : 5
     * id : 170
     * info :
     * name : 第一张
     * productList :
     * productNum :
     * realAmt : 100
     * shopId : 75479513
     * state : 0
     * uuid : 0cc8f5b326ed443c8e973fab396527b5
     */

    private int branchId;
    private int classification;
    private String commodityList;
    private String commodityNum;
    private int expiryDate;
    private int headOfficeId;
    @Id
    private long id;
    private String info;
    private String name;
    private String productList;
    private String productNum;
    private double realAmt;
    private String shopId;
    private int state;
    private String uuid;
    private long requestTime;
    private boolean expand;// 是否展开
    private int isClick = -1;

    @ToMany(referencedJoinProperty = "numCardId")
    private List<CardNumberList2Bean> cardNumberList2;

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 583743405)
    public synchronized void resetCardNumberList2() {
        cardNumberList2 = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1644986693)
    public List<CardNumberList2Bean> getCardNumberList2() {
        if (cardNumberList2 == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CardNumberList2BeanDao targetDao = daoSession.getCardNumberList2BeanDao();
            List<CardNumberList2Bean> cardNumberList2New = targetDao._queryNumCardListInfo_CardNumberList2(id);
            synchronized (this) {
                if(cardNumberList2 == null) {
                    cardNumberList2 = cardNumberList2New;
                }
            }
        }
        return cardNumberList2;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 949993433)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNumCardListInfoDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1439837799)
    private transient NumCardListInfoDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Bindable
    public int getIsClick() {
        return this.isClick;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
        notifyPropertyChanged(BR.expand);
    }
    @Bindable
    public boolean getExpand() {
        return this.expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
        notifyPropertyChanged(BR.expand);
    }

    public long getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public double getRealAmt() {
        return this.realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }

    public String getProductNum() {
        return this.productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductList() {
        return this.productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeadOfficeId() {
        return this.headOfficeId;
    }

    public void setHeadOfficeId(int headOfficeId) {
        this.headOfficeId = headOfficeId;
    }

    public int getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(int expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCommodityNum() {
        return this.commodityNum;
    }

    public void setCommodityNum(String commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getCommodityList() {
        return this.commodityList;
    }

    public void setCommodityList(String commodityList) {
        this.commodityList = commodityList;
    }

    public int getClassification() {
        return this.classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getBranchId() {
        return this.branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @Generated(hash = 2024936688)
    public NumCardListInfo(int branchId, int classification, String commodityList, String commodityNum, int expiryDate, int headOfficeId, long id, String info, String name, String productList, String productNum,
            double realAmt, String shopId, int state, String uuid, long requestTime, boolean expand, int isClick) {
        this.branchId = branchId;
        this.classification = classification;
        this.commodityList = commodityList;
        this.commodityNum = commodityNum;
        this.expiryDate = expiryDate;
        this.headOfficeId = headOfficeId;
        this.id = id;
        this.info = info;
        this.name = name;
        this.productList = productList;
        this.productNum = productNum;
        this.realAmt = realAmt;
        this.shopId = shopId;
        this.state = state;
        this.uuid = uuid;
        this.requestTime = requestTime;
        this.expand = expand;
        this.isClick = isClick;
    }

    @Generated(hash = 1978728433)
    public NumCardListInfo() {
    }

   
}
