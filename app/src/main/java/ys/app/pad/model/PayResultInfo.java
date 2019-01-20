package ys.app.pad.model;

import java.io.Serializable;

/**
 * Created by WBJ on 2018/3/8 15:57.
 *  订单查询请求接口
 */

public class PayResultInfo implements Serializable{

    /**
     * mchNo : 20170623xxxxxx
     * orderNo : 170607150791952588458692608
     * refundNo : 170607150791952588458692608R01
     * gwTradeNo : 170607121391908651589177344
     * timeStamp : 1502079200705
     */

    private String mchNo;
    private String orderNo;
    private String refundNo;
    private String gwTradeNo;
    private String timeStamp;

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getGwTradeNo() {
        return gwTradeNo;
    }

    public void setGwTradeNo(String gwTradeNo) {
        this.gwTradeNo = gwTradeNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
