package ys.app.pad.itemmodel;

import java.util.List;

/**
 * Created by liuyin on 2017/11/14.
 */

public class DailySettlementBean {
    private List<CommodityListBean> commodityList;
    private List<OrderListBean> orderList;
    private List<ProductListBean> productList;
    private List<RechargeListBean> rechargeList;

    public List<CommodityListBean> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<CommodityListBean> commodityList) {
        this.commodityList = commodityList;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public List<RechargeListBean> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargeListBean> rechargeList) {
        this.rechargeList = rechargeList;
    }

    public static class CommodityListBean {
        /**
         * id : 164
         * identification : 1
         * money : 5704.39
         * name : 主粮系列
         */

        private int id;
        private int identification;
        private double money;
        private String name;
        private int number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdentification() {
            return identification;
        }

        public void setIdentification(int identification) {
            this.identification = identification;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class OrderListBean {
        /**
         * id : 12
         * money : 394.86
         * name : 微信
         */

        private int id;
        private double money;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ProductListBean {
        /**
         * id : 134
         * identification : 2
         * money : 140
         * name : 洗澡
         */

        private int id;
        private int identification;
        private double money;
        private String name;
        private int number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdentification() {
            return identification;
        }

        public void setIdentification(int identification) {
            this.identification = identification;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class RechargeListBean {
        /**
         * id : 12
         * money : 394.8
         * name : 微信
         */

        private int id;
        private double money;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
