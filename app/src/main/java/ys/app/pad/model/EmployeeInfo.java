package ys.app.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 作者：lv
 * 时间：2017/3/29 19:12
 */
@Entity
public class EmployeeInfo extends BaseSelect implements Serializable{

    /**
     * branchId : 206453
     * createTime : 1503734091000
     * emergencyContact :
     * emergencyContactPhone :
     * headOfficeId : 5
     * id : 211
     * jobNumber : 01
     * name : 我王
     * password :
     * phone : 13011111111
     * position :
     * post : 前台
     * qq :
     * sex : 1
     * shopId : 238d2ccd
     * shopName :
     * status : 0
     * type : 0
     * userType : 0
     */

    private int branchId;
    private long createTime;
    private String emergencyContact;
    private String emergencyContactPhone;
    private int headOfficeId;
    @Id
    private long id;
    private String jobNumber;
    private String name;
    private String password;
    private String phone;
    private String position;
    private String post;
    private String qq;
    private int sex;
    private String shopId;
    private String shopName;
    private int status;
    private int type;
    private int userType;
    private long requestTime;
    private int select;
    @Generated(hash = 1467226873)
    public EmployeeInfo(int branchId, long createTime, String emergencyContact,
            String emergencyContactPhone, int headOfficeId, long id,
            String jobNumber, String name, String password, String phone,
            String position, String post, String qq, int sex, String shopId,
            String shopName, int status, int type, int userType, long requestTime,
            int select) {
        this.branchId = branchId;
        this.createTime = createTime;
        this.emergencyContact = emergencyContact;
        this.emergencyContactPhone = emergencyContactPhone;
        this.headOfficeId = headOfficeId;
        this.id = id;
        this.jobNumber = jobNumber;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.position = position;
        this.post = post;
        this.qq = qq;
        this.sex = sex;
        this.shopId = shopId;
        this.shopName = shopName;
        this.status = status;
        this.type = type;
        this.userType = userType;
        this.requestTime = requestTime;
        this.select = select;
    }
    @Generated(hash = 1293963402)
    public EmployeeInfo() {
    }
    public long getRequestTime() {
        return this.requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
    public int getUserType() {
        return this.userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getShopName() {
        return this.shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getQq() {
        return this.qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getPost() {
        return this.post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJobNumber() {
        return this.jobNumber;
    }
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getHeadOfficeId() {
        return this.headOfficeId;
    }
    public void setHeadOfficeId(int headOfficeId) {
        this.headOfficeId = headOfficeId;
    }
    public String getEmergencyContactPhone() {
        return this.emergencyContactPhone;
    }
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    public String getEmergencyContact() {
        return this.emergencyContact;
    }
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getBranchId() {
        return this.branchId;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    public int getSelect() {
        return this.select;
    }
    public void setSelect(int select) {
        this.select = select;
    }
   

}
