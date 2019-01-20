package ys.app.pad.model;

/**
 * Created by WBJ on 2018/3/8 13:58.
 *  统一支付返回结果
 */

public class OrderPayResult {

    /**
     * result : SUCCESS
     * msg : 业务请求成功
     * payChannelTypeNo : 0205
     * data : {"gwTradeNo":"20180308169123306019098624","orderNo":"1803051520488599625","qrCode":"weixin://wxpay/bizpayurl?pr=lYPtIJI","qrCodeImg":"https://scan.sandgate.cn:20042/sandpay/api/pay/getQRCode?codeUrl=weixin://wxpay/bizpayurl?pr=lYPtIJI"}
     * timeStamp : 1520488604809
     * sign : A4F0BA9ED626AEBEACDE7073CDA858F4
     */

    private String result;
    private String msg;
    private String payChannelTypeNo;
    private Data data;
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

    public String getPayChannelTypeNo() {
        return payChannelTypeNo;
    }

    public void setPayChannelTypeNo(String payChannelTypeNo) {
        this.payChannelTypeNo = payChannelTypeNo;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

    public static class Data {
        /**
         * gwTradeNo : 20180308169123306019098624
         * orderNo : 1803051520488599625
         * qrCode : weixin://wxpay/bizpayurl?pr=lYPtIJI
         * qrCodeImg : https://scan.sandgate.cn:20042/sandpay/api/pay/getQRCode?codeUrl=weixin://wxpay/bizpayurl?pr=lYPtIJI
         */

        private String gwTradeNo;
        private String orderNo;
        private String qrCode;
        private String qrCodeImg;
        private String completeDate;
        private String amount;
        private String mchNo;

        public String getGwTradeNo() {
            return gwTradeNo;
        }

        public void setGwTradeNo(String gwTradeNo) {
            this.gwTradeNo = gwTradeNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getQrCodeImg() {
            return qrCodeImg;
        }

        public void setQrCodeImg(String qrCodeImg) {
            this.qrCodeImg = qrCodeImg;
        }

        public String getCompleteDate() {
            return completeDate;
        }

        public void setCompleteDate(String completeDate) {
            this.completeDate = completeDate;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMchNo() {
            return mchNo;
        }

        public void setMchNo(String mchNo) {
            this.mchNo = mchNo;
        }
    }

    public static class DataScan {

        /**
         * mchNo : 1234567890
         * amount : 0.01
         * receiptAmount : 0.01
         * buyerAccount : 888***@qq.com
         * orderNo : 201607110025071468167907405
         * tradeNo3rd : 2016071121001004450238372237
         * gwTradeNo : 20160712004944010100420968804
         * storeId : 010203
         * operatorId : 01
         * terminalId : 设备终端号
         * completeDate : 付款时间,格式为（yyyy-MM-dd  HH：mm：ss)
         */

        private String mchNo;
        private String amount;
        private String receiptAmount;
        private String buyerAccount;
        private String orderNo;
        private String tradeNo3rd;
        private String gwTradeNo;
        private String storeId;
        private String operatorId;
        private String terminalId;
        private String completeDate;

        public String getMchNo() {
            return mchNo;
        }

        public void setMchNo(String mchNo) {
            this.mchNo = mchNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getReceiptAmount() {
            return receiptAmount;
        }

        public void setReceiptAmount(String receiptAmount) {
            this.receiptAmount = receiptAmount;
        }

        public String getBuyerAccount() {
            return buyerAccount;
        }

        public void setBuyerAccount(String buyerAccount) {
            this.buyerAccount = buyerAccount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTradeNo3rd() {
            return tradeNo3rd;
        }

        public void setTradeNo3rd(String tradeNo3rd) {
            this.tradeNo3rd = tradeNo3rd;
        }

        public String getGwTradeNo() {
            return gwTradeNo;
        }

        public void setGwTradeNo(String gwTradeNo) {
            this.gwTradeNo = gwTradeNo;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getCompleteDate() {
            return completeDate;
        }

        public void setCompleteDate(String completeDate) {
            this.completeDate = completeDate;
        }
    }

    public static class DataBankCard{

        /**
         * mchNo : 1234567890
         * amount : 0.01
         * buyerAccount : 微信用户  openid
         * orderNo : 201607110025071468167907405
         * tradeNo3rd : 2016071121001004450238372237
         * gwTradeNo : 20160712004944010100420968804
         * terminalId  : 设备终端号
         * completeDate : 付款时间
         */

        private String mchNo;
        private String amount;
        private String buyerAccount;
        private String orderNo;
        private String tradeNo3rd;
        private String gwTradeNo;
        private String terminalId;
        private String completeDate;

        public String getMchNo() {
            return mchNo;
        }

        public void setMchNo(String mchNo) {
            this.mchNo = mchNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBuyerAccount() {
            return buyerAccount;
        }

        public void setBuyerAccount(String buyerAccount) {
            this.buyerAccount = buyerAccount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTradeNo3rd() {
            return tradeNo3rd;
        }

        public void setTradeNo3rd(String tradeNo3rd) {
            this.tradeNo3rd = tradeNo3rd;
        }

        public String getGwTradeNo() {
            return gwTradeNo;
        }

        public void setGwTradeNo(String gwTradeNo) {
            this.gwTradeNo = gwTradeNo;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getCompleteDate() {
            return completeDate;
        }

        public void setCompleteDate(String completeDate) {
            this.completeDate = completeDate;
        }
    }
}
