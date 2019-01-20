package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.greendao.DaoSession;
import com.greendao.GoodTypeInfoDao;
import com.greendao.ServiceTypeInfoDao;
import com.greendao.VipCardInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaa on 2017/3/23.
 */
@Entity
public class VipCardInfo extends  BaseSelect implements Serializable{


    private String productList;
    private String productNum;
    private String commodityList;
    private String commodityNum;
    private String shopId;
    private int expiryDate;
    private double commodityDiscount;
    private double productDiscount;
    private String name;
    @Id
    private long id;
    private String info;
    private long requestTime;
    private int state;
    private int classification;
    private boolean expand;// 是否展开
    private int isClick = -1;

    @ToMany(referencedJoinProperty = "vipCardId")
    private List<GoodTypeInfo> cardDiscountList;

    @ToMany(referencedJoinProperty = "vipCardId")
    private List<ServiceTypeInfo> cardDiscountList2;

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
    @Generated(hash = 311838635)
    public synchronized void resetCardDiscountList2() {
        cardDiscountList2 = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 545464199)
    public List<ServiceTypeInfo> getCardDiscountList2() {
        if (cardDiscountList2 == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceTypeInfoDao targetDao = daoSession.getServiceTypeInfoDao();
            List<ServiceTypeInfo> cardDiscountList2New = targetDao._queryVipCardInfo_CardDiscountList2(id);
            synchronized (this) {
                if(cardDiscountList2 == null) {
                    cardDiscountList2 = cardDiscountList2New;
                }
            }
        }
        return cardDiscountList2;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1054725035)
    public synchronized void resetCardDiscountList() {
        cardDiscountList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1787766819)
    public List<GoodTypeInfo> getCardDiscountList() {
        if (cardDiscountList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GoodTypeInfoDao targetDao = daoSession.getGoodTypeInfoDao();
            List<GoodTypeInfo> cardDiscountListNew = targetDao._queryVipCardInfo_CardDiscountList(id);
            synchronized (this) {
                if(cardDiscountList == null) {
                    cardDiscountList = cardDiscountListNew;
                }
            }
        }
        return cardDiscountList;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 442159408)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVipCardInfoDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1996341611)
    private transient VipCardInfoDao myDao;
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

    public int getClassification() {
        return this.classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProductDiscount() {
        return this.productDiscount;
    }

    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public double getCommodityDiscount() {
        return this.commodityDiscount;
    }

    public void setCommodityDiscount(double commodityDiscount) {
        this.commodityDiscount = commodityDiscount;
    }

    public int getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(int expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    @Generated(hash = 795924214)
    public VipCardInfo(String productList, String productNum, String commodityList,
            String commodityNum, String shopId, int expiryDate,
            double commodityDiscount, double productDiscount, String name, long id,
            String info, long requestTime, int state, int classification,
            boolean expand, int isClick) {
        this.productList = productList;
        this.productNum = productNum;
        this.commodityList = commodityList;
        this.commodityNum = commodityNum;
        this.shopId = shopId;
        this.expiryDate = expiryDate;
        this.commodityDiscount = commodityDiscount;
        this.productDiscount = productDiscount;
        this.name = name;
        this.id = id;
        this.info = info;
        this.requestTime = requestTime;
        this.state = state;
        this.classification = classification;
        this.expand = expand;
        this.isClick = isClick;
    }

    @Generated(hash = 625701091)
    public VipCardInfo() {
    }

   
}
