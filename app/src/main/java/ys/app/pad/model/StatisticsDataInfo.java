package ys.app.pad.model;

import java.util.List;

/**
 * Created by aaa on 2017/6/8.
 */

public class StatisticsDataInfo {

    /**
     * realAmt : 0
     * rechargeAmt : 0
     * productTypeList : [{"totalAmt":0,"imgurl":"/Test/images/circle_blue.png","cardType":null,"allcount":null,"shopId":"182011","count":null,"name":"养护服务","id":1},{"totalAmt":0,"imgurl":"/Test/images/circle_blue.png","cardType":null,"allcount":null,"shopId":"182011","count":null,"name":"寄养","id":2},{"totalAmt":0,"imgurl":"/Test/images/circle_blue.png","cardType":null,"allcount":null,"shopId":"182011","count":null,"name":"活体","id":3}]
     * payMethodList : [{"code":"1001","totalAmt":0,"name":"现金","id":1},{"code":"1003","totalAmt":0,"name":"微信","id":2},{"code":"1004","totalAmt":0,"name":"支付宝","id":3},{"code":"1005","totalAmt":0,"name":"百度钱包","id":4},{"code":"1006","totalAmt":0,"name":"银行卡","id":5},{"code":"1007","totalAmt":0,"name":"易付宝","id":6},{"code":"1009","totalAmt":0,"name":"京东钱包","id":7},{"code":"1011","totalAmt":0,"name":"QQ钱包","id":8},{"code":"8888","totalAmt":0,"name":"会员卡","id":9}]
     * vipAmt : null
     */

    private double realAmt;
    private double rechargeAmt;
    private double vipAmt;
    private List<ProductTypeListBean> productTypeList;
    private List<PayMethodListBean> payMethodList;

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

    public double getVipAmt() {
        return vipAmt;
    }

    public void setVipAmt(double vipAmt) {
        this.vipAmt = vipAmt;
    }

    public List<ProductTypeListBean> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductTypeListBean> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public List<PayMethodListBean> getPayMethodList() {
        return payMethodList;
    }

    public void setPayMethodList(List<PayMethodListBean> payMethodList) {
        this.payMethodList = payMethodList;
    }

    public static class ProductTypeListBean {
        /**
         * totalAmt : 0
         * imgurl : /Test/images/circle_blue.png
         * cardType : null
         * allcount : null
         * shopId : 182011
         * count : null
         * name : 养护服务
         * id : 1
         */

        private double totalAmt;
        private String imgurl;
        private Object cardType;
        private double allcount;
        private String shopId;
        private double count;
        private String name;
        private double id;

        public double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(double totalAmt) {
            this.totalAmt = totalAmt;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public Object getCardType() {
            return cardType;
        }

        public void setCardType(Object cardType) {
            this.cardType = cardType;
        }

        public double getAllcount() {
            return allcount;
        }

        public void setAllcount(double allcount) {
            this.allcount = allcount;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }
    }

    public static class PayMethodListBean {
        /**
         * code : 1001
         * totalAmt : 0
         * name : 现金
         * id : 1
         */

        private String code;
        private double totalAmt;
        private String name;
        private double id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(double totalAmt) {
            this.totalAmt = totalAmt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }
    }
}
