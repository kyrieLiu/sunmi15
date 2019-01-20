package ys.app.pad.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ys.app.pad.AppApplication;

/**
 * Created by lyy on 2017/2/6 14:42.
 * email：2898049851@qq.com
 */

public class SpUtil {
    private static final Context mcontext;
    public static final String Share_Preferences_Name_Mode_Private = "sp_private";
    public static final String Key_Is_First_Into_APP = "Key_Is_First_Into_APP";// 是否是第一次进入APP
    public static final String Key_Is_Login = "Key_Is_Login";// 是否已经登录
    public static final String Key_Login_Time = "Key_Login_Time";//设置登陆时间
    public static final String Key_headOffice_Id="Key_headOffice_Id";//总店ID
    public static final String Key_branch_Id="Key_branch_Id";//分店ID
    private static final String Key_shop_name="Key_shop_name";//店铺名称
    public static final String Key_equipment_shop_id="Key_equipment_shop_id";
    public static final String Key_SAND_mchNo="Key_SAND_mchNo";
    public static final String Key_SAND_key="Key_SAND_key";
    public static final String Key_VERSION_MODULE="Key_VERSION_MODULE";//板块等级
    public static final String Key_MODEL_VICE="Key_MODEL_VICE";//副屏类型
    public static final String Key_Store_ID="Key_Store_ID";//杉德店铺ID
    public static final String Key_Receipt_Setting="Key_Receipt_Setting";//是否打印会员价

    static {
        mcontext = AppApplication.getInstance().getApplicationContext();
    }

    /**
     * 该配置文件被自己的应用程序访问
     */
    public static SharedPreferences getPreferences() {
        if (mcontext != null) {
            return mcontext.getSharedPreferences(
                    Share_Preferences_Name_Mode_Private,
                    Context.MODE_PRIVATE);
        }
        return null;
    }


    /**
     * 设置是否已经登录
     *
     * @param flag
     * @return
     */
    public static boolean setIsLogin(boolean flag) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putBoolean(Key_Is_Login, flag).commit();
        }
        return false;
    }

    /**
     * 获取是否打印会员价
     *
     * @return
     */
    public static boolean getReceiptPrinterVip() {
        if (getPreferences() != null) {
            return getPreferences().getBoolean(Key_Receipt_Setting, false);
        }
        return false;
    }

    /**
     * 设置是否打印会员价
     */
    public static boolean setReceiptPrinterVip(boolean printVip) {
        if (getPreferences() != null) {
            return getPreferences().edit().putBoolean(Key_Receipt_Setting, printVip).commit();
        }
        return false;
    }


    /**
     * 获取商铺Id
     *
     * @return
     */
    public static String getShopId() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_equipment_shop_id, "");
        }
        return "";
    }
    /**
     * 总店ID
     * @param headOfficeId
     */
    public static boolean setHeadOfficeId(int headOfficeId) {
        if (getPreferences() != null) {
            return getPreferences().edit().putInt(Key_headOffice_Id, headOfficeId).commit();
        }
        return false;

    }
    /**
     * 获取总店ID
     */
    public static int getHeadOfficeId() {
        if (getPreferences() != null) {
            return getPreferences().getInt(Key_headOffice_Id, -1);
        }
        return -1;
    }

    /**
     * 分店ID
     * @param branchId
     */
    public static boolean setBranchId(int branchId) {
        if (getPreferences() != null) {
            return getPreferences().edit().putInt(Key_branch_Id, branchId).commit();
        }
        return false;

    }
    /**
     * 获取分店ID
     *
     * @return
     */
    public static int getBranchId() {
        if (getPreferences() != null) {
            return getPreferences().getInt(Key_branch_Id, -1);
        }
        return -1;
    }
    /**
     * 设置店铺名称
     *
     * @return
     */
    public static boolean setShopName(String shopName) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putString(Key_shop_name, shopName).commit();
        }
        return false;
    }

    /**
     * 获取店铺名称
     *
     * @return
     */
    public static String getShopName() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_shop_name,"");
        }
        return "";
    }

    /**
     * 设置是否第一次进入APP
     *
     * @return
     */
    public static boolean setIsFirtIntoAPP(boolean isFirst) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putBoolean(Key_Is_First_Into_APP, isFirst).commit();
        }
        return false;
    }

    /**
     * 获取是否第一次进入APP
     *
     * @return
     */
    public static boolean getIsFirtIntoAPP() {
        if (getPreferences() != null) {
            return getPreferences().getBoolean(Key_Is_First_Into_APP, true);
        }
        return true;
    }
    //存储杉德商户号
    public static boolean setSandMchNo(String mchNo) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putString(Key_SAND_mchNo, mchNo).commit();
        }
        return false;
    }
    public static String getSandMchNo() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_SAND_mchNo,"");
        }
        return "";
    }
    //存储杉德秘钥
    public static boolean setSandKey(String key) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putString(Key_SAND_key, key).commit();
        }
        return false;
    }
    public static String getSandKey() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_SAND_key,"");
        }
        return "";
    }



    //存储杉德商户ID
    public static boolean setStoreID(String key) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putString(Key_Store_ID, key).commit();
        }
        return false;
    }
    public static String getStoreId() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_Store_ID,"");
        }
        return "";
    }

    //存储机器型号
//    public static boolean setModelType(String type) {
//        if (getPreferences() != null) {
//            return getPreferences().edit()
//                    .putString(Key_MODEL_TYPE, type).commit();
//        }
//        return false;
//    }
//    public static String getModelType() {
//        if (getPreferences() != null) {
//            return getPreferences().getString(Key_MODEL_TYPE,"");
//        }
//        return "";
//    }
    //存储副屏型号
    public static boolean setModelVice(String type) {
        if (getPreferences() != null) {
            return getPreferences().edit()
                    .putString(Key_MODEL_VICE, type).commit();
        }
        return false;
    }
    public static String getModelVice() {
        if (getPreferences() != null) {
            return getPreferences().getString(Key_MODEL_VICE,"");
        }
        return "";
    }


    //存储版本模块
    public static boolean setVersionModule(int versionModule) {
        if (getPreferences() != null) {
            return getPreferences().edit().putInt(Key_VERSION_MODULE, versionModule).commit();
        }
        return false;

    }
    public static int getVersionModule() {
        if (getPreferences() != null) {
            return getPreferences().getInt(Key_VERSION_MODULE, -1);
        }
        return -1;
    }
    /**
     * 获取是否已经登录
     *
     * @return
     */
    public static boolean getIsLogin() {
        if (getPreferences() != null) {
            return getPreferences().getBoolean(Key_Is_Login, false);
        }
        return false;
    }

    /**
     * 设置商铺Id
     *
     * @param shopId
     * @return
     */
    public static boolean setShopId(String shopId) {
        if (getPreferences() != null) {
            return getPreferences().edit().putString(Key_equipment_shop_id, shopId).commit();
        }
        return false;
    }

    public static void clearLoginInfo() {
        getPreferences().edit()
                .remove(Key_Is_Login)
//                .remove(Key_Employee_Num)
                .remove(Key_Login_Time)
                .commit();
    }
}
