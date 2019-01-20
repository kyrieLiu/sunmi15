package ys.app.pad.model;

/**
 * Created by liuyin on 2017/10/27.
 */

public class EmployeeAppointmentInfo {
    /**
     * appointmentDay : 1508947200000
     * appointmentPoint : ,8:00,9:00,10:00,11:00,12:00,16:00,17:00,18:00,19:00,20:00,21:00,
     * branchId : 206453
     * headOfficeId : 5
     * id : 321
     * productTypeIdStr : ,134,135,136,137,
     * productTypeNameStr : ,洗澡,美容,SPA,寄养,
     * shopId : ####
     * state : 0
     * userId : 211
     * userName : 我王
     */

    private long appointmentDay;
    private String appointmentPoint;
    private int branchId;
    private int headOfficeId;
    private int id;
    private String productTypeIdStr;
    private String productTypeNameStr;
    private String shopId;
    private int state;
    private int userId;
    private String userName;

    public long getAppointmentDay() {
        return appointmentDay;
    }

    public void setAppointmentDay(long appointmentDay) {
        this.appointmentDay = appointmentDay;
    }

    public String getAppointmentPoint() {
        return appointmentPoint;
    }

    public void setAppointmentPoint(String appointmentPoint) {
        this.appointmentPoint = appointmentPoint;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
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

    public String getProductTypeIdStr() {
        return productTypeIdStr;
    }

    public void setProductTypeIdStr(String productTypeIdStr) {
        this.productTypeIdStr = productTypeIdStr;
    }

    public String getProductTypeNameStr() {
        return productTypeNameStr;
    }

    public void setProductTypeNameStr(String productTypeNameStr) {
        this.productTypeNameStr = productTypeNameStr;
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
}
