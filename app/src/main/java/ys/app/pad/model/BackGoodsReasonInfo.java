package ys.app.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by aaa on 2017/4/1.
 */
@Entity
public class BackGoodsReasonInfo extends BaseSelect{


    /**
     * shopId : 1
     * reasons : 过期
     * id : 2
     */

    private String shopId;
    private String reasons;
    @Id
    private Long id;
    private long requestTime;
    public long getRequestTime() {
        return this.requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getReasons() {
        return this.reasons;
    }
    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    @Generated(hash = 1475620657)
    public BackGoodsReasonInfo(String shopId, String reasons, Long id,
            long requestTime) {
        this.shopId = shopId;
        this.reasons = reasons;
        this.id = id;
        this.requestTime = requestTime;
    }
    @Generated(hash = 2062155106)
    public BackGoodsReasonInfo() {
    }

}