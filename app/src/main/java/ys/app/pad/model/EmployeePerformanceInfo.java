package ys.app.pad.model;

import java.util.List;

/**
 * Created by liuyin on 2017/8/11.
 */

public class EmployeePerformanceInfo {
    private List<OrderDetailedListCommodityBean> orderDetailedListCommodity;
    private List<OrderDetailedListProductBean> orderDetailedListProduct;
    private List<RechargeListBean> rechargeList;

    public List<OrderDetailedListCommodityBean> getOrderDetailedListCommodity() {
        return orderDetailedListCommodity;
    }

    public void setOrderDetailedListCommodity(List<OrderDetailedListCommodityBean> orderDetailedListCommodity) {
        this.orderDetailedListCommodity = orderDetailedListCommodity;
    }

    public List<OrderDetailedListProductBean> getOrderDetailedListProduct() {
        return orderDetailedListProduct;
    }

    public void setOrderDetailedListProduct(List<OrderDetailedListProductBean> orderDetailedListProduct) {
        this.orderDetailedListProduct = orderDetailedListProduct;
    }

    public List<RechargeListBean> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargeListBean> rechargeList) {
        this.rechargeList = rechargeList;
    }

    public static class OrderDetailedListCommodityBean {
        /**
         * branchId : 206453
         * count : 1
         * countMoney : 0.01
         * countPromotionMoney : 0
         * countVipMoney : 0.01
         * createDate : 1505442637000
         * headOfficeId : 2
         * id : 3813
         * img : img
         * info :
         * isFold : 0
         * isGift : 0
         * isPromotion : 0
         * name : 测试
         * orderId : 20170915103037544549
         * payDate : 1505442642000
         * price : 0.01
         * productId : 2409
         * productType : 164
         * productTypeName : 主粮系列
         * promotionNum : 0
         * promotionPrice : 0
         * promotionType : 0
         * realMoney : 0.01
         * shopId : 206453
         * shopName :
         * state : 1
         * type : 1
         * unit :
         * userId : 246
         * userName : 刘隐
         * vipPrice : 0.01
         * vipUserId : 269
         * vipUserName : 刘先生
         * vipUserPhone : 15566369958
         */

        private int branchId;
        private int count;
        private double countMoney;
        private double countPromotionMoney;
        private double countVipMoney;
        private long createDate;
        private int headOfficeId;
        private int id;
        private String img;
        private String info;
        private int isFold;
        private int isGift;
        private int isPromotion;
        private String name;
        private String orderId;
        private long payDate;
        private double price;
        private int productId;
        private int productType;
        private String productTypeName;
        private double promotionNum;
        private double promotionPrice;
        private int promotionType;
        private double realMoney;
        private String shopId;
        private String shopName;
        private int state;
        private int type;
        private String unit;
        private int userId;
        private String userName;
        private double vipPrice;
        private int vipUserId;
        private String vipUserName;
        private String vipUserPhone;
        private int isClassification;

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getCountMoney() {
            return countMoney;
        }

        public void setCountMoney(double countMoney) {
            this.countMoney = countMoney;
        }

        public double getCountPromotionMoney() {
            return countPromotionMoney;
        }

        public void setCountPromotionMoney(double countPromotionMoney) {
            this.countPromotionMoney = countPromotionMoney;
        }

        public void setCountPromotionMoney(int countPromotionMoney) {
            this.countPromotionMoney = countPromotionMoney;
        }



        public int getIsClassification() {
            return isClassification;
        }

        public void setIsClassification(int isClassification) {
            this.isClassification = isClassification;
        }


        public double getCountVipMoney() {
            return countVipMoney;
        }

        public void setCountVipMoney(double countVipMoney) {
            this.countVipMoney = countVipMoney;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getHeadOfficeId() {
            return headOfficeId;
        }

        public void setHeadOfficeId(int headOfficeId) {
            this.headOfficeId = headOfficeId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getIsFold() {
            return isFold;
        }

        public void setIsFold(int isFold) {
            this.isFold = isFold;
        }

        public int getIsGift() {
            return isGift;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        public int getIsPromotion() {
            return isPromotion;
        }

        public void setIsPromotion(int isPromotion) {
            this.isPromotion = isPromotion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public int getPromotionType() {
            return promotionType;
        }

        public void setPromotionType(int promotionType) {
            this.promotionType = promotionType;
        }

        public double getRealMoney() {
            return realMoney;
        }

        public void setRealMoney(double realMoney) {
            this.realMoney = realMoney;
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

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public int getVipUserId() {
            return vipUserId;
        }

        public void setVipUserId(int vipUserId) {
            this.vipUserId = vipUserId;
        }

        public String getVipUserName() {
            return vipUserName;
        }

        public void setVipUserName(String vipUserName) {
            this.vipUserName = vipUserName;
        }

        public String getVipUserPhone() {
            return vipUserPhone;
        }

        public void setVipUserPhone(String vipUserPhone) {
            this.vipUserPhone = vipUserPhone;
        }
    }

    public static class OrderDetailedListProductBean {
        /**
         * branchId : 206453
         * count : 1
         * countMoney : 25
         * countPromotionMoney : 25
         * countVipMoney : 25
         * createDate : 1505444631000
         * headOfficeId : 2
         * id : 3820
         * img : img
         * info :
         * isFold : 0
         * isGift : 0
         * isPromotion : 1
         * name : 剪毛
         * orderId : 20170915110351848681
         * payDate : 1505444649000
         * price : 50
         * productId : 475
         * productType : 134
         * productTypeName : 洗澡
         * promotionNum : 0.5
         * promotionPrice : 25
         * promotionType : 1
         * realMoney : 25
         * shopId : 206453
         * shopName :
         * state : 1
         * type : 2
         * unit :
         * userId : 246
         * userName : 刘隐
         * vipPrice : 25
         * vipUserId : 0
         * vipUserName :
         * vipUserPhone :
         */

        private int branchId;
        private int count;
        private double countMoney;
        private double countPromotionMoney;
        private double countVipMoney;
        private long createDate;
        private int headOfficeId;
        private int id;
        private String img;
        private String info;
        private int isFold;
        private int isGift;
        private int isPromotion;
        private String name;
        private String orderId;
        private long payDate;
        private double price;
        private int productId;
        private int productType;
        private String productTypeName;
        private double promotionNum;
        private double promotionPrice;
        private int promotionType;
        private double realMoney;
        private String shopId;
        private String shopName;
        private int state;
        private int type;
        private String unit;
        private int userId;
        private String userName;
        private double vipPrice;
        private int vipUserId;
        private String vipUserName;
        private String vipUserPhone;
        private int isClassification;

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getCountMoney() {
            return countMoney;
        }

        public void setCountMoney(double countMoney) {
            this.countMoney = countMoney;
        }

        public double getCountPromotionMoney() {
            return countPromotionMoney;
        }

        public void setCountPromotionMoney(double countPromotionMoney) {
            this.countPromotionMoney = countPromotionMoney;
        }

        public double getCountVipMoney() {
            return countVipMoney;
        }

        public void setCountVipMoney(double countVipMoney) {
            this.countVipMoney = countVipMoney;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getHeadOfficeId() {
            return headOfficeId;
        }

        public void setHeadOfficeId(int headOfficeId) {
            this.headOfficeId = headOfficeId;
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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getIsFold() {
            return isFold;
        }

        public void setIsFold(int isFold) {
            this.isFold = isFold;
        }

        public int getIsGift() {
            return isGift;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        public int getIsPromotion() {
            return isPromotion;
        }

        public void setIsPromotion(int isPromotion) {
            this.isPromotion = isPromotion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public int getPromotionType() {
            return promotionType;
        }

        public void setPromotionType(int promotionType) {
            this.promotionType = promotionType;
        }

        public double getRealMoney() {
            return realMoney;
        }

        public void setRealMoney(double realMoney) {
            this.realMoney = realMoney;
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

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getVipPrice() {
            return vipPrice;
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public int getVipUserId() {
            return vipUserId;
        }

        public void setVipUserId(int vipUserId) {
            this.vipUserId = vipUserId;
        }

        public String getVipUserName() {
            return vipUserName;
        }

        public void setVipUserName(String vipUserName) {
            this.vipUserName = vipUserName;
        }

        public String getVipUserPhone() {
            return vipUserPhone;
        }

        public void setVipUserPhone(String vipUserPhone) {
            this.vipUserPhone = vipUserPhone;
        }
    }

    public static class RechargeListBean {
        /**
         * afterAmt : 0.01
         * beforeAmt : 0
         * branchId : 206453
         * dotime : 1505440733000
         * headOfficeId : 2
         * id : 499
         * orderId : 20170915095853246475
         * payDate : 1505440791000
         * payWay : 0012
         * realAmt : 0.01
         * rechargeAmt : 0.01
         * shopId : 206453
         * state : 1
         * userId : 246
         * userName : 刘隐
         * vipName : 刘先生
         * vipPhone : 15566369958
         * vipUserCardNo : 20170915095817727404
         * vipUserId : 269
         */

        private double afterAmt;
        private double beforeAmt;
        private int branchId;
        private long dotime;
        private int headOfficeId;
        private int id;
        private String orderId;
        private long payDate;
        private String payWay;
        private double realAmt;
        private double rechargeAmt;
        private String shopId;
        private int state;
        private int userId;
        private String userName;
        private String vipName;
        private String vipPhone;
        private String vipUserCardNo;
        private int vipUserId;

        public double getAfterAmt() {
            return afterAmt;
        }

        public void setAfterAmt(double afterAmt) {
            this.afterAmt = afterAmt;
        }

        public double getBeforeAmt() {
            return beforeAmt;
        }

        public void setBeforeAmt(double beforeAmt) {
            this.beforeAmt = beforeAmt;
        }

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public long getDotime() {
            return dotime;
        }

        public void setDotime(long dotime) {
            this.dotime = dotime;
        }

        public int getHeadOfficeId() {
            return headOfficeId;
        }

        public void setHeadOfficeId(int headOfficeId) {
            this.headOfficeId = headOfficeId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public String getPayWay() {
            return payWay;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }

        public double getRealAmt() {
            return realAmt;
        }

        public void setRealAmt(double realAmt) {
            this.realAmt = realAmt;
        }

        public double getRechargeAmt() {
            return rechargeAmt;
        }

        public void setRechargeAmt(double rechargeAmt) {
            this.rechargeAmt = rechargeAmt;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public String getVipUserCardNo() {
            return vipUserCardNo;
        }

        public void setVipUserCardNo(String vipUserCardNo) {
            this.vipUserCardNo = vipUserCardNo;
        }

        public int getVipUserId() {
            return vipUserId;
        }

        public void setVipUserId(int vipUserId) {
            this.vipUserId = vipUserId;
        }
    }
}
