package ys.app.pad.model;

/**
 * Created by WBJ on 2018/3/9 09:13.
 * 撤销订单返回结果
 */

public class OrderCancelResult {

    /**
     * mchNo : 1234567890
     * result : SUCCESS
     * msg : 订单撤销成功
     * orderNo : 201609260242091474828929296
     * gwTradeNo : 2016092602421101189961784
     * retryFlag : N
     * sign : ec973e7dd03637c6b78248b767ec3991
     * timeStamp : 1474828953775
     * tradeStatus : refund
     */

    private String mchNo;
    private String result;
    private String msg;
    private String orderNo;
    private String gwTradeNo;
    private String retryFlag;
    private String sign;
    private String timeStamp;
    private String tradeStatus;

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGwTradeNo() {
        return gwTradeNo;
    }

    public void setGwTradeNo(String gwTradeNo) {
        this.gwTradeNo = gwTradeNo;
    }

    public String getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(String retryFlag) {
        this.retryFlag = retryFlag;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
