package ys.app.pad.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaa on 2017/4/25.
 */

public class OrderInfo extends BaseObservable implements Serializable {

    /**
     * shopId : 1
     * orderNo : 20170421100213084348
     * vipUserId : 2
     * userId : 100
     * realAmt : 10
     * orderTime : 1492740135000
     * totalAmt : 0
     * favouredMethod :
     * favouredAmt : 0.9
     * tradeNum : 0
     * tradeList :
     * payedMethod :
     * payedTime : null
     * favouredBill : 0
     * favouredBillId : 0
     * vipName : 杨颖
     * vipPhone : 18239900618
     * rechargeId : 0
     * orderInfo : 订单
     * orderDetailedList : [{"shopId":"1","shopName":"","vipUserId":2,"unit":"","countMoney":10,"createDate":1492740133000,"productId":1,"productType":1,"productTypeName":"",
     * "price":10,"vipPrice":0.9,"img":"/Test/images/rectangle_blue.png","orderId":"20170421100213084348","userId":100,"countVipMoney":0.9,"count":1,"orderInfo":"","name":"狗粮A",
     * "id":101,"state":0,"type":1}]
     *  "rechargeList": [
     {
     "dotime": 1497662967000,
     "vipUserId": 9,
     "orderId": "20170617092927913708",
     "realAmt": 369.00,
     "userId": 101,
     "payDate": 1497663113000,
     "vipUserCardNo": "20170505115643679465",
     "rechargeAmt": 369.00,
     "payWay": "1001",
     "vipName": "潇潇",
     "vipPhone": "15968800865",
     "beforeAmt": 0.00,
     "afterAmt": 369.00,
     "shopId": "182011",
     "userName": null,
     "id": 142,
     "state": 1
     }
     ]
     * status : 0
     * id : 46
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
    private String cashierTradeNo;
    private int isRefund;
    private String previousOrderNo;
    private int isClassification;
    private int cardId;
    private String randomOrderNo;

    public String getRandomOrderNo() {
        return randomOrderNo;
    }

    public void setRandomOrderNo(String randomOrderNo) {
        this.randomOrderNo = randomOrderNo;
    }

    public void setPayedTime(long payedTime) {
        this.payedTime = payedTime;
    }

    public String getVipCardNo() {
        return vipCardNo;
    }

    public void setVipCardNo(String vipCardNo) {
        this.vipCardNo = vipCardNo;
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

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    public String getCashierTradeNo() {
        return cashierTradeNo;
    }

    public void setCashierTradeNo(String cashierTradeNo) {
        this.cashierTradeNo = cashierTradeNo;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public String getPreviousOrderNo() {
        return previousOrderNo;
    }

    public void setPreviousOrderNo(String previousOrderNo) {
        this.previousOrderNo = previousOrderNo;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    private double afterAmt;
    private String status;
    private int id;
    private List<OrderDetailedListBean> orderDetailedList;
    private List<RechargeListBean> rechargeList;


    private boolean expand;// 是否展开
    private int isClick = -1;

    @Bindable
    public int getIsClick() {
        return isClick;
    }

    /**
     * -1 默认没有动画
     * 0 打开动画
     * 1 关闭动画
     *
     * @param isClick
     */
    public void setClick(int isClick) {
        this.isClick = isClick;
        notifyPropertyChanged(ys.app.pad.BR.isClick);
    }

    @Bindable
    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
        notifyPropertyChanged(BR.expand);
    }

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

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
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

    public List<OrderDetailedListBean> getOrderDetailedList() {
        return orderDetailedList;
    }

    public void setOrderDetailedList(List<OrderDetailedListBean> orderDetailedList) {
        this.orderDetailedList = orderDetailedList;
    }

    public long getPayedTime() {
        return payedTime;
    }

    public List<RechargeListBean> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargeListBean> rechargeList) {
        this.rechargeList = rechargeList;
    }

    public static class RechargeListBean extends BaseObservable implements Serializable {
        private long dotime;
        private int vipUserId;
        private String orderId;
        private double realAmt;
        private int userId;
        private long payDate;
        private String vipUserCardNo;
        private double rechargeAmt;
        private String payWay;
        private String vipName;
        private String vipPhone;
        private double beforeAmt;
        private double afterAmt;
        private String shopId;
        private String userName;
        private int id;
        private int state;
        public void setDotime(long dotime) {
            this.dotime = dotime;
        }
        public long getDotime() {
            return dotime;
        }

        public void setVipUserId(int vipUserId) {
            this.vipUserId = vipUserId;
        }
        public int getVipUserId() {
            return vipUserId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getOrderId() {
            return orderId;
        }

        public void setRealAmt(double realAmt) {
            this.realAmt = realAmt;
        }
        public double getRealAmt() {
            return realAmt;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }
        public long getPayDate() {
            return payDate;
        }

        public void setVipUserCardNo(String vipUserCardNo) {
            this.vipUserCardNo = vipUserCardNo;
        }
        public String getVipUserCardNo() {
            return vipUserCardNo;
        }

        public void setRechargeAmt(double rechargeAmt) {
            this.rechargeAmt = rechargeAmt;
        }
        public double getRechargeAmt() {
            return rechargeAmt;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }
        public String getPayWay() {
            return payWay;
        }

        public void setVipName(String vipName) {
            this.vipName = vipName;
        }
        public String getVipName() {
            return vipName;
        }

        public void setVipPhone(String vipPhone) {
            this.vipPhone = vipPhone;
        }
        public String getVipPhone() {
            return vipPhone;
        }

        public void setBeforeAmt(double beforeAmt) {
            this.beforeAmt = beforeAmt;
        }
        public double getBeforeAmt() {
            return beforeAmt;
        }

        public void setAfterAmt(double afterAmt) {
            this.afterAmt = afterAmt;
        }
        public double getAfterAmt() {
            return afterAmt;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
        public String getShopId() {
            return shopId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
            notifyPropertyChanged(BR.userName);
        }
        @Bindable
        public String getUserName() {
            return userName;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setState(int state) {
            this.state = state;
        }
        public int getState() {
            return state;
        }

    }

    public static class OrderDetailedListBean extends BaseObservable implements Serializable {
        /**
         * shopId : 1
         * shopName :
         * vipUserId : 2
         * unit :
         * countMoney : 10
         * createDate : 1492740133000
         * productId : 1
         * productType : 1
         * productTypeName :
         * price : 10
         * vipPrice : 0.9
         * img : /Test/images/rectangle_blue.png
         * orderId : 20170421100213084348
         * userId : 100
         * countVipMoney : 0.9
         * count : 1
         * orderInfo :
         * name : 狗粮A
         * id : 101
         * state : 0
         * type : 1
         */

        private String shopId;
        private String shopName;
        private int vipUserId;
        private String unit;
        private long createDate;
        private int productId;
        private int productType;
        private String productTypeName;
        private double price;
        private double vipPrice;
        private String img;
        private String orderId;
        private int userId;
        private int count;
        private String info;
        private String name;
        private String userName;
        private int id;
        private int state;
        private int isPromotion;
        private int type;
        private int isRefund;
        private int isGift;
        private int afterNum;
        private int beforeNum;
        private String vipCardNo;
        private int isClassification;
        private double realMoney;
        private double vipAmt;

        public int getIsGift() {
            return isGift;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        public int getIsClassification() {
            return isClassification;
        }

        public void setIsClassification(int isClassification) {
            this.isClassification = isClassification;
        }

        public String getVipCardNo() {
            return vipCardNo;
        }

        public void setVipCardNo(String vipCardNo) {
            this.vipCardNo = vipCardNo;
        }

        public int getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(int isRefund) {
            this.isRefund = isRefund;
        }

        public double getRealMoney() {
            return realMoney;
        }

        public void setRealMoney(double realMoney) {
            this.realMoney = realMoney;
        }

        public int getIsPromotion() {
            return isPromotion;
        }

        public void setIsPromotion(int isPromotion) {
            this.isPromotion = isPromotion;
        }

        public int getIsFold() {
            return isFold;
        }

        public void setIsFold(int isFold) {
            this.isFold = isFold;
        }

        public double getPromotionNum() {
            return promotionNum;
        }

        public void setPromotionNum(double promotionNum) {
            this.promotionNum = promotionNum;
        }

        public double getPromotionPrice() {
            return promotionPrice;
        }

        public void setPromotionPrice(double promotionPrice) {
            this.promotionPrice = promotionPrice;
        }

        public double getCountPromotionMoney() {
            return countPromotionMoney;
        }

        public void setCountPromotionMoney(double countPromotionMoney) {
            this.countPromotionMoney = countPromotionMoney;
        }

        public int getAfterNum() {
            return afterNum;
        }

        public void setAfterNum(int afterNum) {
            this.afterNum = afterNum;
        }

        public int getBeforeNum() {
            return beforeNum;
        }

        public void setBeforeNum(int beforeNum) {
            this.beforeNum = beforeNum;
        }

        public double getVipAmt() {
            return vipAmt;
        }

        public void setVipAmt(double vipAmt) {
            this.vipAmt = vipAmt;
        }

        private int isFold;
        private double promotionNum;
        private double promotionPrice;
        private double countPromotionMoney;
        private double countMoney;
        private double countVipMoney;

        @Bindable
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
           notifyPropertyChanged(ys.app.pad.BR.userName);
        }

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
