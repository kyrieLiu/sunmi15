package ys.app.pad.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WBJ on 2018/3/7 17:06.
 *  统一支付请求参数
 */

public class OrderPayParam implements Serializable {
    /**
     * mchNo : 20170623xxxxxx
     * orderNo : 170607103791884519388483584
     * amount : 0.01
     * goodsName : 付款码支付测试  DEMO
     * goodsDesc : [{"goods_id":"000001","goods_name":"杉德测试商品","quantity":1,"price":1}]
     * payChannelTypeNo : 0103
     * operatorId : xxx
     * storeId : xxx
     * terminalId : 99990000
     * timeStamp : 1502073444009
     * sign : 8E4B85A555E0277F992BF875E67CCCEA
     */

    private String mchNo;
    private String orderNo;
    private String amount;
    private String goodsName;
    private String payChannelTypeNo;
//    private String operatorId;
//    private String storeId;
//    private String terminalId;
    private String timeStamp;
    private List<GoodsDesc> goodsDesc;
    private String authCode;                    //特殊条码、刷卡支付必填
//    private String discountableAmount;         //非必填项  可打折金额
//    private String undiscountableAmount;        //非必填项，不可打着金额
//    private String overtime;                    //非必填，交易超时时间 单位为正整数的分钟数
    private String sign;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

//    public String getDiscountableAmount() {
//        return discountableAmount;
//    }

//    public void setDiscountableAmount(String discountableAmount) {
//        this.discountableAmount = discountableAmount;
//    }

//    public String getUndiscountableAmount() {
//        return undiscountableAmount;
//    }

//    public void setUndiscountableAmount(String undiscountableAmount) {
//        this.undiscountableAmount = undiscountableAmount;
//    }

//    public String getOvertime() {
//        return overtime;
//    }

//    public void setOvertime(String overtime) {
//        this.overtime = overtime;
//    }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPayChannelTypeNo() {
        return payChannelTypeNo;
    }

    public void setPayChannelTypeNo(String payChannelTypeNo) {
        this.payChannelTypeNo = payChannelTypeNo;
    }

//    public String getOperatorId() {
//        return operatorId;
//    }

//    public void setOperatorId(String operatorId) {
//        this.operatorId = operatorId;
//    }

//    public String getStoreId() {
//        return storeId;
//    }

//    public void setStoreId(String storeId) {
//        this.storeId = storeId;
//    }

//    public String getTerminalId() {
//        return terminalId;
//    }

//    public void setTerminalId(String terminalId) {
//        this.terminalId = terminalId;
//    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<GoodsDesc> getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(List<GoodsDesc> goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class GoodsDesc implements Serializable{
        /**
         * goods_id : 000001
         * goods_name : 杉德测试商品
         * quantity : 1
         * price : 1
         */

        private String goods_id;
        private String goods_name;
        private int quantity;                   //商品数量
        private double price;                      //商品单价，单位为分

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }



}
