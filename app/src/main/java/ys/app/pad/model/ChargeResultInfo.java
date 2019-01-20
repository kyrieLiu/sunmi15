package ys.app.pad.model;

import java.util.List;

/**
 * Created by aaa on 2017/4/20.
 */

public class ChargeResultInfo {

    /**
     * shopId : 1
     * orderNo : 20170502032345280916
     * vipUserId : 0
     * userId : 100
     * realAmt : 150
     * orderTime : 1493709827000
     * totalAmt : 0
     * favouredMethod :
     * favouredAmt : 150
     * tradeNum : 0
     * tradeList :
     * payedMethod : 1001
     * payedTime : 1493709843000
     * favouredBill : 0
     * favouredBillId : 0
     * vipName :
     * vipPhone :
     * vipCardNo : null
     * rechargeId : 0
     * orderInfo : 订单
     * beforeAmt : null
     * afterAmt : null
     * orderDetailedList : [{"shopId":"1","shopName":"","vipUserId":0,"unit":"","countMoney":150,"createDate":1493709825000,"productId":2,"productType":1,"productTypeName":"美容","price":150,"vipPrice":150,"img":"/Test/images/rectangle_blue.png","orderId":"20170502032345280916","userId":100,"countVipMoney":150,"payDate":1493709843000,"count":1,"info":"","name":"中狗美容","id":172,"state":1,"type":2}]
     * status : 1
     * id : 124
     */

    private String shopId;
    private String orderNo;
    private int vipUserId;
    private String userId;
    private double realAmt;
    private long orderTime;
    private double totalAmt;
    private String favouredMethod;
    private double favouredAmt;
    private int tradeNum;
    private String tradeList;
    private String payedMethod;
    private long payedTime;
    private int favouredBill;
    private int favouredBillId;
    private String vipName;
    private String vipPhone;
    private String vipCardNo;
    private int rechargeId;
    private String orderInfo;
    private double beforeAmt;
    private double afterAmt;
    private String status;
    private int id;
    private int isClassification;
    private List<OrderDetailedListBean> rechargeList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getVipUserId() {
        return vipUserId;
    }

    public void setVipUserId(int vipUserId) {
        this.vipUserId = vipUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(double realAmt) {
        this.realAmt = realAmt;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getFavouredMethod() {
        return favouredMethod;
    }

    public void setFavouredMethod(String favouredMethod) {
        this.favouredMethod = favouredMethod;
    }

    public double getFavouredAmt() {
        return favouredAmt;
    }

    public void setFavouredAmt(double favouredAmt) {
        this.favouredAmt = favouredAmt;
    }

    public int getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(int tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getTradeList() {
        return tradeList;
    }

    public void setTradeList(String tradeList) {
        this.tradeList = tradeList;
    }

    public String getPayedMethod() {
        return payedMethod;
    }

    public void setPayedMethod(String payedMethod) {
        this.payedMethod = payedMethod;
    }

    public long getPayedTime() {
        return payedTime;
    }

    public void setPayedTime(long payedTime) {
        this.payedTime = payedTime;
    }

    public int getFavouredBill() {
        return favouredBill;
    }

    public void setFavouredBill(int favouredBill) {
        this.favouredBill = favouredBill;
    }

    public int getFavouredBillId() {
        return favouredBillId;
    }

    public void setFavouredBillId(int favouredBillId) {
        this.favouredBillId = favouredBillId;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getVipPhone() {
        return vipPhone;
    }

    public void setVipPhone(String vipPhone) {
        this.vipPhone = vipPhone;
    }

    public String getVipCardNo() {
        return vipCardNo;
    }

    public void setVipCardNo(String vipCardNo) {
        this.vipCardNo = vipCardNo;
    }

    public int getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(int rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public double getBeforeAmt() {
        return beforeAmt;
    }

    public void setBeforeAmt(double beforeAmt) {
        this.beforeAmt = beforeAmt;
    }

    public double getAfterAmt() {
        return afterAmt;
    }

    public void setAfterAmt(double afterAmt) {
        this.afterAmt = afterAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsClassification() {
        return isClassification;
    }

    public void setIsClassification(int isClassification) {
        this.isClassification = isClassification;
    }

    public List<OrderDetailedListBean> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<OrderDetailedListBean> rechargeList) {
        this.rechargeList = rechargeList;
    }

    public static class OrderDetailedListBean {
        /**
         * shopId : 1
         * shopName :
         * vipUserId : 0
         * unit :
         * countMoney : 150
         * createDate : 1493709825000
         * productId : 2
         * productType : 1
         * productTypeName : 美容
         * price : 150
         * vipPrice : 150
         * img : /Test/images/rectangle_blue.png
         * orderId : 20170502032345280916
         * userId : 100
         * countVipMoney : 150
         * payDate : 1493709843000
         * count : 1
         * info :
         * name : 中狗美容
         * id : 172
         * state : 1
         * type : 2
         */

        private String shopId;
        private String shopName;
        private int vipUserId;
        private String unit;
        private int countMoney;
        private long createDate;
        private int productId;
        private int productType;
        private String productTypeName;
        private int price;
        private int vipPrice;
        private String img;
        private String orderId;
        private int userId;
        private int countVipMoney;
        private long payDate;
        private int count;
        private String info;
        private String name;
        private int id;
        private int state;
        private int type;
        private double presentAmt;
        private String userName;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getVipUserId() {
            return vipUserId;
        }

        public void setVipUserId(int vipUserId) {
            this.vipUserId = vipUserId;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getCountMoney() {
            return countMoney;
        }

        public void setCountMoney(int countMoney) {
            this.countMoney = countMoney;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getProductType() {
            return productType;
        }

        public void setProductType(int productType) {
            this.productType = productType;
        }

        public String getProductTypeName() {
            return productTypeName;
        }

        public void setProductTypeName(String productTypeName) {
            this.productTypeName = productTypeName;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(int vipPrice) {
            this.vipPrice = vipPrice;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getCountVipMoney() {
            return countVipMoney;
        }

        public void setCountVipMoney(int countVipMoney) {
            this.countVipMoney = countVipMoney;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getPresentAmt() {
            return presentAmt;
        }

        public void setPresentAmt(double presentAmt) {
            this.presentAmt = presentAmt;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
