package ys.app.pad.model;

import java.io.Serializable;

/**
 * Created by WBJ on 2018/3/8 16:09.
 *  订单查询返回结果
 */

public class OrderQueryResult implements Serializable{

    /**
     * result : 返回码
     * msg : 返回码结果说明
     * orderNo : 201605081234567890
     * gwTradeNo : 201605081234567890
     * tradeNo3rd : 201605081234567890123
     * mchNo : 1234567890
     * payAmount : 0.01
     * payStatus : SUCCESS
     * goodsDesc : 商品描述
     * payChannelTypeNo : 0101
     * createDate : 2016-05-0612:34:50
     * payDate : 2016-05-0612:34:50
     * refundStatus : 2016-05-0612:34:50
     * refundAmount : 2016-05-0612:34:50
     * refunDate : 2016-05-0612:34:50
     * originalRefundStatus : 第三方支付机构原始状态
     * originalPayStatusDesc : 第三方机构返回的原始状态描述
     * buyerAccount : 买家账号
     * sellerAccount : 卖家账号
     * timeStamp : 1497504380000
     * sign : 326E59040FB8D21149DAF43E88E4C12A
     */

    private String result;
    private String msg;
    private String orderNo;
    private String gwTradeNo;
    private String tradeNo3rd;
    private String mchNo;
    private String payAmount;
    private String payStatus;
    private String goodsDesc;
    private String payChannelTypeNo;
    private String createDate;
    private String payDate;
    private String refundStatus;
    private String refundAmount;
    private String refunDate;
    private String originalRefundStatus;
    private String originalPayStatusDesc;
    private String buyerAccount;
    private String sellerAccount;
    private String timeStamp;
    private String sign;

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

    public String getTradeNo3rd() {
        return tradeNo3rd;
    }

    public void setTradeNo3rd(String tradeNo3rd) {
        this.tradeNo3rd = tradeNo3rd;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getPayChannelTypeNo() {
        return payChannelTypeNo;
    }

    public void setPayChannelTypeNo(String payChannelTypeNo) {
        this.payChannelTypeNo = payChannelTypeNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefunDate() {
        return refunDate;
    }

    public void setRefunDate(String refunDate) {
        this.refunDate = refunDate;
    }

    public String getOriginalRefundStatus() {
        return originalRefundStatus;
    }

    public void setOriginalRefundStatus(String originalRefundStatus) {
        this.originalRefundStatus = originalRefundStatus;
    }

    public String getOriginalPayStatusDesc() {
        return originalPayStatusDesc;
    }

    public void setOriginalPayStatusDesc(String originalPayStatusDesc) {
        this.originalPayStatusDesc = originalPayStatusDesc;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
