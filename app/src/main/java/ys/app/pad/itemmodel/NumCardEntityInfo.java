package ys.app.pad.itemmodel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuyin on 2017/12/14.
 */
@Entity
public class NumCardEntityInfo {
    @Id(autoincrement = true)
    private Long dbId;
    private long id;
    private int num;
    private String name;
    private double realAmt;
    private int type;
    private String typeName;
    public String getTypeName() {
        return this.typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Long getDbId() {
        return this.dbId;
    }
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    @Generated(hash = 1468698968)
    public NumCardEntityInfo(Long dbId, long id, int num, String name,
            double realAmt, int type, String typeName) {
        this.dbId = dbId;
        this.id = id;
        this.num = num;
        this.name = name;
        this.realAmt = realAmt;
        this.type = type;
        this.typeName = typeName;
    }
    @Generated(hash = 1397648334)
    public NumCardEntityInfo() {
    }

}
