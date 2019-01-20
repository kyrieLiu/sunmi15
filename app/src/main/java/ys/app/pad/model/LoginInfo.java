package ys.app.pad.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by jkh on 16/5/9.
 */
@Entity
public class LoginInfo implements Serializable {


    /**
     * province : null
     * provinceName : 北京
     * city : null
     * cityName : 北京市
     * area : null
     * equipmentId : 238d2ccd
     * shopId : 182011
     * shopName : 北京宠贝佳宠物服务有限公司
     * areaName : 大兴区
     * phone : null
     * password : e10adc3949ba59abbe56e057f20f883e
     * address : 格雷众创园
     * id : 3
     */

    private String province;
    private String provinceName;
    private String city;
    private String cityName;
    private String area;
    private String equipmentId;
    private String shopId;
    private String shopName;
    private String shopShortName;
    private String areaName;
    private String phone;
    private String password;
    private int branchId;
    private int headOfficeId;
    private String address;
    private String mchNo;
    private String md5Key;
    private String storeId;
    private int isModular;
    @Id(autoincrement = true)
    private Long id;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getHeadOfficeId() {
        return this.headOfficeId;
    }
    public void setHeadOfficeId(int headOfficeId) {
        this.headOfficeId = headOfficeId;
    }
    public int getBranchId() {
        return this.branchId;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAreaName() {
        return this.areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public String getShopShortName() {
        return this.shopShortName;
    }
    public void setShopShortName(String shopShortName) {
        this.shopShortName = shopShortName;
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
    public String getEquipmentId() {
        return this.equipmentId;
    }
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getCityName() {
        return this.cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getProvinceName() {
        return this.provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public int getIsModular() {
        return this.isModular;
    }
    public void setIsModular(int isModular) {
        this.isModular = isModular;
    }

    @Generated(hash = 1482572712)
    public LoginInfo(String province, String provinceName, String city, String cityName,
            String area, String equipmentId, String shopId, String shopName,
            String shopShortName, String areaName, String phone, String password,
            int branchId, int headOfficeId, String address, String mchNo, String md5Key,
            String storeId, int isModular, Long id) {
        this.province = province;
        this.provinceName = provinceName;
        this.city = city;
        this.cityName = cityName;
        this.area = area;
        this.equipmentId = equipmentId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopShortName = shopShortName;
        this.areaName = areaName;
        this.phone = phone;
        this.password = password;
        this.branchId = branchId;
        this.headOfficeId = headOfficeId;
        this.address = address;
        this.mchNo = mchNo;
        this.md5Key = md5Key;
        this.storeId = storeId;
        this.isModular = isModular;
        this.id = id;
    }
    @Generated(hash = 1911824992)
    public LoginInfo() {
    }
   
}
