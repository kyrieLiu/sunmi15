package ys.app.pad.model;

import android.os.Handler;
import android.view.ActionMode;

/**
 * Created by liuyin on 2017/12/11.
 */

public class RefundVipInfo {
    /**
     * presentAmt : 10
     * realAmt : 52
     * refundAmt : 43.16
     * nowAmt : 52
     */

    private double presentAmt;
    private double realAmt;
    private double refundAmt;
    private double nowAmt;

    public double getPresentAmt() {
        return presentAmt;
    }

    public void setPresentAmt(double presentAmt) {
        this.presentAmt = presentAmt;
    }

    public double getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }

    public double getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(double refundAmt) {
        this.refundAmt = refundAmt;
    }

    public double getNowAmt() {
        return nowAmt;
    }

    public void setNowAmt(double nowAmt) {
        this.nowAmt = nowAmt;
    }
}
