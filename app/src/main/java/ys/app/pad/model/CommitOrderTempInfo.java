package ys.app.pad.model;

/**
 * Created by aaa on 2017/3/29.
 */
public class CommitOrderTempInfo {
    private long id;
    private long type;
    private double price;//原价
    private int num;
    private long userId;
    private String name;
    private String userName;
    private int isPromotion;
    private double promotionAmt;
    private int isGift;
    private int orderType;//0是正常单,1是挂单后取出的单
    private int cardID;//次卡ID
    private int originNum;//商品原有数量
    private int vipUserId;
    public int getVipUserId() {
        return this.vipUserId;
    }
    public void setVipUserId(int vipUserId) {
        this.vipUserId = vipUserId;
    }
    public int getOriginNum() {
        return this.originNum;
    }
    public void setOriginNum(int originNum) {
        this.originNum = originNum;
    }
    public int getCardID() {
        return this.cardID;
    }
    public void setCardID(int cardID) {
        this.cardID = cardID;
    }
    public int getOrderType() {
        return this.orderType;
    }
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    public int getIsGift() {
        return this.isGift;
    }
    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }
    public double getPromotionAmt() {
        return this.promotionAmt;
    }
    public void setPromotionAmt(double promotionAmt) {
        this.promotionAmt = promotionAmt;
    }
    public int getIsPromotion() {
        return this.isPromotion;
    }
    public void setIsPromotion(int isPromotion) {
        this.isPromotion = isPromotion;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public long getType() {
        return this.type;
    }
    public void setType(long type) {
        this.type = type;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public CommitOrderTempInfo(long id, long type,
            double price, int num, long userId, String name,
            String userName, int isPromotion, double promotionAmt, int isGift, int orderType, int cardID, int originNum, int vipUserId) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.num = num;
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.isPromotion = isPromotion;
        this.promotionAmt = promotionAmt;
        this.isGift = isGift;
        this.orderType = orderType;
        this.cardID = cardID;
        this.originNum = originNum;
        this.vipUserId = vipUserId;
    }
    public CommitOrderTempInfo() {
    }


    @Override
    public boolean equals(Object obj) {
        CommitOrderTempInfo info= (CommitOrderTempInfo) obj;
        boolean result=false;
        if (promotionAmt!=0){
            result=id==info.getId()&&promotionAmt==info.getPromotionAmt()&&type==info.getType()&&isGift==info.getIsGift()&&userId==info.getUserId();
        }else if (price!=0){
            result=id==info.getId()&&price==info.getPrice()&&type==info.getType()&&isGift==info.getIsGift()&&userId==info.getUserId();
        }else if (cardID!=0){
            if (id!=0){
                result=id==info.getId()&&type==info.getType()&&cardID==info.getCardID()&&userId==info.getUserId();
            }else{
                result=type==info.getType()&&cardID==info.getCardID();
            }
        }
        return result;
    }
}
