package ys.app.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.greendao.DaoSession;
import com.greendao.AnimalTypeInfoDao;
import com.greendao.AnimalTypeClassifyInfoDao;

/**
 * Created by aaa on 2017/3/25.
 */

@Entity
public class AnimalTypeInfo {


    /**
     * varietiesList : [{"name":"小猫","id":4,"type":1},{"name":"小瘦猫","id":5,"type":1}]
     * name : 猫
     * id : 1
     */

    private String name;
    @Id
    private long id;
    // 对多，referencedJoinProperty 指定实体中与外联实体属性相对应的外键
    @ToMany(referencedJoinProperty = "animalTypeId")
    private List<AnimalTypeClassifyInfo> varietiesList;
    private long requestTime;
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
    @Generated(hash = 911982404)
    public synchronized void resetVarietiesList() {
        varietiesList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 927402578)
    public List<AnimalTypeClassifyInfo> getVarietiesList() {
        if (varietiesList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnimalTypeClassifyInfoDao targetDao = daoSession.getAnimalTypeClassifyInfoDao();
            List<AnimalTypeClassifyInfo> varietiesListNew = targetDao._queryAnimalTypeInfo_VarietiesList(id);
            synchronized (this) {
                if(varietiesList == null) {
                    varietiesList = varietiesListNew;
                }
            }
        }
        return varietiesList;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 667075621)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnimalTypeInfoDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 2005092057)
    private transient AnimalTypeInfoDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
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
    public long getRequestTime() {
        return this.requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
    @Generated(hash = 1775443670)
    public AnimalTypeInfo(String name, long id, long requestTime) {
        this.name = name;
        this.id = id;
        this.requestTime = requestTime;
    }
    @Generated(hash = 1890893751)
    public AnimalTypeInfo() {
    }



}
