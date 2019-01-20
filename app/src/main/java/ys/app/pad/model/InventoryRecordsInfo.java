package ys.app.pad.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/12.
 */

public class InventoryRecordsInfo extends BaseSelect implements Serializable {



    /**
     * dotime : 1502872699000
     * id : 11
     * inventoryList : [{"commodityAmt":0.01,"commodityId":1334,"commodityName":"测试","dotime":1502872699000,"id":1405,"inventoryNum":10,"shopId":"204078","stockNum":975,"type":2,"userId":0,"uuid":"10305c78e6ee461b84553c54dd160a0e"}]
     * inventoryList2 : [{"commodityAmt":0.01,"commodityId":1334,"commodityName":"测试","dotime":1502872699000,"id":1405,"inventoryNum":10,"shopId":"204078","stockNum":975,"type":2,"userId":0,"uuid":"10305c78e6ee461b84553c54dd160a0e"}]
     * inventoryLosses : 9.65
     * inventoryProfit : 0
     * shopId : 204078
     * uuid : 10305c78e6ee461b84553c54dd160a0e
     */

    private long dotime;
    private int id;
    private double inventoryLosses;
    private double inventoryProfit;
    private String shopId;
    private String uuid;
    private List<InventoryListBean> inventoryList;
    private List<InventoryList2Bean> inventoryList2;

    public long getDotime() {
        return dotime;
    }

    public void setDotime(long dotime) {
        this.dotime = dotime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getInventoryLosses() {
        return inventoryLosses;
    }

    public void setInventoryLosses(double inventoryLosses) {
        this.inventoryLosses = inventoryLosses;
    }

    public double getInventoryProfit() {
        return inventoryProfit;
    }

    public void setInventoryProfit(double inventoryProfit) {
        this.inventoryProfit = inventoryProfit;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<InventoryListBean> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<InventoryListBean> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<InventoryList2Bean> getInventoryList2() {
        return inventoryList2;
    }

    public void setInventoryList2(List<InventoryList2Bean> inventoryList2) {
        this.inventoryList2 = inventoryList2;
    }

    public static class InventoryListBean implements Serializable{
        /**
         * commodityAmt : 0.01
         * commodityId : 1334
         * commodityName : 测试
         * dotime : 1502872699000
         * id : 1405
         * inventoryNum : 10
         * shopId : 204078
         * stockNum : 975
         * type : 2
         * userId : 0
         * uuid : 10305c78e6ee461b84553c54dd160a0e
         */

        private double commodityAmt;
        private int commodityId;
        private String commodityName;
        private long dotime;
        private int id;
        private int inventoryNum;
        private String shopId;
        private int stockNum;
        private int type;
        private int userId;
        private String uuid;

        public double getCommodityAmt() {
            return commodityAmt;
        }

        public void setCommodityAmt(double commodityAmt) {
            this.commodityAmt = commodityAmt;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public long getDotime() {
            return dotime;
        }

        public void setDotime(long dotime) {
            this.dotime = dotime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInventoryNum() {
            return inventoryNum;
        }

        public void setInventoryNum(int inventoryNum) {
            this.inventoryNum = inventoryNum;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public int getStockNum() {
            return stockNum;
        }

        public void setStockNum(int stockNum) {
            this.stockNum = stockNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    public static class InventoryList2Bean implements Serializable{
        /**
         * commodityAmt : 0.01
         * commodityId : 1334
         * commodityName : 测试
         * dotime : 1502872699000
         * id : 1405
         * inventoryNum : 10
         * shopId : 204078
         * stockNum : 975
         * type : 2
         * userId : 0
         * uuid : 10305c78e6ee461b84553c54dd160a0e
         */

        private double commodityAmt;
        private int commodityId;
        private String commodityName;
        private long dotime;
        private int id;
        private int inventoryNum;
        private String shopId;
        private int stockNum;
        private int type;
        private int userId;
        private String uuid;

        public double getCommodityAmt() {
            return commodityAmt;
        }

        public void setCommodityAmt(double commodityAmt) {
            this.commodityAmt = commodityAmt;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public long getDotime() {
            return dotime;
        }

        public void setDotime(long dotime) {
            this.dotime = dotime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInventoryNum() {
            return inventoryNum;
        }

        public void setInventoryNum(int inventoryNum) {
            this.inventoryNum = inventoryNum;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public int getStockNum() {
            return stockNum;
        }

        public void setStockNum(int stockNum) {
            this.stockNum = stockNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
