package ys.app.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by aaa on 2017/3/25.
 */
@Entity
public class AnimalTypeClassifyInfo  extends  BaseSelect implements Serializable {

    /**
     * name : 小猫
     * id : 4
     * type : 1
     */

    private String name;
    @Id
    private long id;
    private int type;
    private long animalTypeId;
    private long requestTime;
    private String sortLetters; // 索引字母
    public long getAnimalTypeId() {
        return this.animalTypeId;
    }
    public void setAnimalTypeId(long animalTypeId) {
        this.animalTypeId = animalTypeId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
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
    public long getRequestTime() {
        return this.requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
    public String getSortLetters() {
        return this.sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
    @Generated(hash = 918232211)
    public AnimalTypeClassifyInfo(String name, long id, int type,
            long animalTypeId, long requestTime, String sortLetters) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.animalTypeId = animalTypeId;
        this.requestTime = requestTime;
        this.sortLetters = sortLetters;
    }
    @Generated(hash = 2049384486)
    public AnimalTypeClassifyInfo() {
    }

}
