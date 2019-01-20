package ys.app.pad.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaa on 2017/6/9.
 */

public class VipPayInfo implements Serializable{


    /**
     * shopId : 182011
     * payedMethod : 8888
     * payedTime : 1494572155000
     * favouredBill : 0
     * favouredBillId : 0
     * vipName : 杨颖
     * vipPhone : 18239900618
     * vipCardNo : 20170324034434293159
     * rechargeId : 0
     * orderInfo : 订单
     * beforeAmt : 99.65
     * afterAmt : 98.75
     * orderDetailedList : [{"shopName":"","shopId":"182011","vipUserId":2,"unit":"","countMoney":10,"createDate":1494572140000,"productId":22,"productType":3,"productTypeName":"衣服狗窝","price":10,"vipPrice":0.9,"img":"/commodity/193cb8e28752407fa4a50034a97b01cb.jpg","orderId":"20170512025540259451","userId":101,"countVipMoney":0.9,"payDate":1494572155000,"count":1,"info":"","name":"狗狗洗澡盆","id":232,"state":1,"type":1},{"shopName":"","shopId":"182011","vipUserId":2,"unit":"","countMoney":0.02,"createDate":1494572140000,"productId":22,"productType":3,"productTypeName":"衣服狗窝","price":0.02,"vipPrice":0,"img":"/commodity/193cb8e28752407fa4a50034a97b01cb.jpg","orderId":"20170512025540259451","userId":101,"countVipMoney":0,"payDate":1494572155000,"count":1,"info":"","name":"狗狗洗澡盆","id":233,"state":1,"type":1}]
     * orderNo : 20170512025540259451
     * vipUserId : 2
     * userId : 0
     * realAmt : 10.02
     * totalAmt : 0
     * orderTime : 1494572149000
     * favouredMethod :
     * favouredAmt : 0.9
     * tradeNum : 0
     * tradeList :
     * status : 1
     * id : 165
     */

    private String shopId;
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
    private String orderNo;
    private int vipUserId;
    private String userId;
    private double realAmt;
    private double totalAmt;
    private long orderTime;
    private String favouredMethod;
    private double favouredAmt;
    private int tradeNum;
    private String tradeList;
    private String status;
    private int id;
    private List<OrderDetailedListBean> orderDetailedList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
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

    public List<OrderDetailedListBean> getOrderDetailedList() {
        return orderDetailedList;
    }

    public void setOrderDetailedList(List<OrderDetailedListBean> orderDetailedList) {
        this.orderDetailedList = orderDetailedList;
    }

    public static class OrderDetailedListBean {
        /**
         * shopName :
         * shopId : 182011
         * vipUserId : 2
         * unit :
         * countMoney : 10
         * createDate : 1494572140000
         * productId : 22
         * productType : 3
         * productTypeName : 衣服狗窝
         * price : 10
         * vipPrice : 0.9
         * img : /commodity/193cb8e28752407fa4a50034a97b01cb.jpg
         * orderId : 20170512025540259451
         * userId : 101
         * countVipMoney : 0.9
         * payDate : 1494572155000
         * count : 1
         * info :
         * name : 狗狗洗澡盆
         * id : 232
         * state : 1
         * type : 1
         */

        private String shopName;
        private String shopId;
        private int vipUserId;
        private String unit;
        private double countMoney;
        private long createDate;
        private int productId;
        private int productType;
        private String productTypeName;
        private double price;
        private double vipPrice;
        private String img;
        private String orderId;
        private int userId;
        private double countVipMoney;
        private long payDate;
        private int count;
        private String info;
        private String name;
        private int id;
        private int state;
        private int type;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
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

        public double getCountMoney() {
            return countMoney;
        }

        public void setCountMoney(double countMoney) {
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
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

        public double getCountVipMoney() {
            return countVipMoney;
        }

        public void setCountVipMoney(double countVipMoney) {
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
    }
}
