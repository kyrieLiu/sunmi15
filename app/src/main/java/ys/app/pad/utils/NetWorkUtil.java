package ys.app.pad.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetWorkUtil {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMobileNet(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkINfo = cm.getActiveNetworkInfo();
            if (networkINfo != null
                    && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isWifiNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

/*   使用NetworkInfo的getType()方法可以判断是WiFi还是手机网络。手机网络的情况下，使用NetworkInfo的getSubtype()方法去和TelephonyManager的网络类型常量值去比较，判断是何种具体网络。
    TelephonyManager的网络类型常量值如下：
    1、NETWORK_TYPE_1xRTT：         常量值：7        网络类型：1xRTT
    2、NETWORK_TYPE_CDMA ：         常量值：4       网络类型： CDMA （电信2g）
    3、NETWORK_TYPE_EDGE：          常量值：2       网络类型：EDGE（移动2g）
    4、NETWORK_TYPE_EHRPD：      常量值：14    网络类型：eHRPD
    5、NETWORK_TYPE_EVDO_0：      常量值：5     网络类型：EVDO  版本0.（电信3g）
    6、NETWORK_TYPE_EVDO_A：      常量值：6     网络类型：EVDO   版本A （电信3g）
    7、NETWORK_TYPE_EVDO_B：      常量值：12   网络类型：EVDO  版本B（电信3g）
    8、NETWORK_TYPE_GPRS：          常量值：1         网络类型：GPRS （联通2g）
    9、NETWORK_TYPE_HSDPA：        常量值：8      网络类型：HSDPA（联通3g）
    10、NETWORK_TYPE_HSPA：         常量值：10     网络类型：HSPA
    11、NETWORK_TYPE_HSPAP：       常量值：15   网络类型：HSPA+
    12、NETWORK_TYPE_HSUPA：       常量值：9     网络类型：HSUPA
    13、NETWORK_TYPE_IDEN：          常量值：11      网络类型：iDen
    14、NETWORK_TYPE_LTE：             常量值：13       网络类型：LTE(3g到4g的一个过渡，称为准4g)
    15、NETWORK_TYPE_UMTS：          常量值：3    网络类型：UMTS（联通3g）
    16、NETWORK_TYPE_UNKNOWN：常量值：0  网络类型：未知*/

    public static boolean is4GNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null) {
            int subtype = networkINfo.getSubtype();
            if (subtype == TelephonyManager.NETWORK_TYPE_LTE) {
                return true;
            }
        }
        return false;
    }

    public static boolean is2GNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null) {
            int subtype = networkINfo.getSubtype();
            if (subtype == TelephonyManager.NETWORK_TYPE_GPRS ||
                    subtype == TelephonyManager.NETWORK_TYPE_EDGE) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            type = "无可用网络";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "WIFI";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2G";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3G";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4G";
            }
        }
        return type;
    }

    //是否有可用网络
    public static boolean hasAvailableNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
        	return false;
        }
        return true;
    }
    public static String getCurrentCardType(Context context) {
        String cardType = "";
        //获得系统服务，从而取得sim数据
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获得手机SIMType
        int type = tm.getNetworkType();
        //判断类型值，并且命名

        if (type == TelephonyManager.NETWORK_TYPE_UMTS) {

            cardType = "USIM";//类型为UMTS定义为wcdma的USIM卡

        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {

            cardType = " SIM";//类型为GPRS定义为GPRS的SIM卡

        } else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {

            cardType = " SIM";//类型为EDGE定义为EDGE的SIM卡

        } else {

            cardType = " UIM";//类型为unknown定义为cdma的UIM卡

        }
        if (type== TelephonyManager.NETWORK_TYPE_LTE){
            cardType ="4G";
        }
        return cardType;
    }
}
