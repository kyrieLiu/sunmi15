package ys.app.pad.model;

/**
 * Created by WBJ on 2018/3/8 16:55.
 * 退款返回结果
 */

public class RefundResult {


    /**
     * result : SUCCESS
     * msg : 业务请求成功
     * payChannelTypeNo : 0205
     * timeStamp : 1521089139791
     * sign : E93D97FC329781445D1D9C52DAE8685A
     * data : {"completeDate":"2018-03-15 00:45:37","gwTradeNo":"20180315171642127174995968","gwTradeNoPay":"20180315171640601333338112","mchNo":"666859950000923","orderNo":"20180315123931551425_179","payAmount":"0.01","refundAmount":"0.01","refundNo":"201803061521089138765","tradeNo3rd":"20180315124537232677"}
     */

    private String result;
    private String msg;
    private String payChannelTypeNo;
    private String timeStamp;
    private String sign;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * completeDate : 2018-03-15 00:45:37
         * gwTradeNo : 20180315171642127174995968
         * gwTradeNoPay : 20180315171640601333338112
         * mchNo : 666859950000923
         * orderNo : 20180315123931551425_179
         * payAmount : 0.01
         * refundAmount : 0.01
         * refundNo : 201803061521089138765
         * tradeNo3rd : 20180315124537232677
         */

        private String completeDate;
        private String gwTradeNo;
        private String gwTradeNoPay;
        private String mchNo;
        private String orderNo;
        private String payAmount;
        private String refundAmount;
        private String refundNo;
        private String tradeNo3rd;

        public String getCompleteDate() {
            return completeDate;
        }

        public void setCompleteDate(String completeDate) {
            this.completeDate = completeDate;
        }

        public String getGwTradeNo() {
            return gwTradeNo;
        }

        public void setGwTradeNo(String gwTradeNo) {
            this.gwTradeNo = gwTradeNo;
        }

        public String getGwTradeNoPay() {
            return gwTradeNoPay;
        }

        public void setGwTradeNoPay(String gwTradeNoPay) {
            this.gwTradeNoPay = gwTradeNoPay;
        }

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

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(String refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getTradeNo3rd() {
            return tradeNo3rd;
        }

        public void setTradeNo3rd(String tradeNo3rd) {
            this.tradeNo3rd = tradeNo3rd;
        }
    }
}
