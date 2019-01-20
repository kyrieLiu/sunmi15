package ys.app.pad.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;


/**
 * 基本信息服务类
 * <p>例如：
 * <ul>
 * <li>获取AndroidManifest.xml文件中配置的appkey {@code getMetaAppkey(context)}；</li>
 * <li>获取AndroidManifest.xml文件中配置的channel {@code getMetaChannel(context)}</li>
 * <li>获取移动设备的mac地址 {@code getMacAddr(context)}；</li>
 * <li>获取移动设备的设备ID {@code getDeviceId(context)}；</li>
 * <li>获取md5加密之后的设备ID {@code getDeviceIdMd5(context)}；</li>
 * <li>获取网络提供商名称 {@code getNetworkCarrierName(context)}；</li>
 * <li>检查某应用是否安装  {@code checkApp(packageName, context)}；</li>
 * <li>检查应用的资源文件配置是否为 zh_CN本地化配置 {@code isChinaForLocale(context)}；</li>
 * <li>检查应用的UI布局是否为纵向（垂直）定向 {@code isPortrait(context)}；</li>
 * <li>获取应用的versionCode {@code getVersionCode(context)}；</li>
 * <li>获取应用的versionName {@code getVersionName(context)}；</li>
 * <li>检查应用权限 {@code checkPermission(context, permName)}；</li>
 * <li>获取应用包名 {@code getPackageName(context)}；</li>
 * <li>获取应用名称  {@code getAppLabel(context)}；</li>
 * <li>获取处理器（CPU） {@code getProcessor()}；</li>
 * <li>获取显示器分辨率（高*宽） {@code getResolution()}；</li>
 * <li>获取网络类型  {@code getNetworkType(context)}；</li>
 * <li>判断网络是否已连接且为WIFI {@code isWifi(context)}；</li>
 * <li>判断网络是否已连接 {@code isConnected(context)}；</li>
 * <li>判断外部存储是否已安装（SD卡） {@code isMountedForExternalStorage()}；</li>
 * <li>当前设备当前时间停留在几点钟 {@code getLocaleHour(context)}；</li>
 * <li>获取本地信息（国家和语言） {@code getLocaleInfo(context)}；</li>
 * @author dennis
 * @email dennis.pengjianjun@gmail.com
 * @since 1.0
 */
public class AppInfoUtils {

    protected static final String BASIC_SERVICE_CLASS_NAME = AppInfoUtils.class.getName();
    /** 网络类型 - 无（未连接） */
    public static final String NETWORK_TYPE_NO = "";
    /** 网络类型-mobile */
    public static final String NETWORK_TYPE_MOBILE = "2G/3G/4G";
    /** 网络类型-WIFI */
    public static final String NETWORK_TYPE_WIFI = "Wi-Fi";
    /** 默认本地小时数 */
    public static final int DEFAULT_LOCALE_HOUR = 8;

    /**
     * 检查某应用是否安装
     * @param packageName
     *                     应用包名
     * @param context
     *                   应用上下文
     * @return true if package exists, otherwise is false.
     */
    public static boolean checkApp(String packageName, Context context)
    {
        PackageManager pm = context.getPackageManager();
        boolean hasPackage = false;
        try
        {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            hasPackage = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            hasPackage = false;
        }
        return hasPackage;
    }


    /**
     * 检查应用的资源文件配置是否为 zh_CN本地化配置
     * @param context
     *                应用上下文
     * @return true if locale is China, otherwise is false.
     */
    public static boolean isChinaForLocale(Context context)
    {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.toString().equals(Locale.CHINA.toString());
    }

    /**
     * 检查应用的UI布局是否为纵向（垂直）定向
     * @param context
     *                应用上下文
     * @return
     */
    public static boolean isPortrait(Context context)
    {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用的versionCode
     * @param context
     *                应用上下文
     * @return
     */
    public static String getVersionCode(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            return String.valueOf(versionCode);
        }
        catch (PackageManager.NameNotFoundException e) {}
        return "";
    }

    /**
     * 获取应用的versionName
     * @param context
     *                应用上下文
     * @return
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        }
        catch (PackageManager.NameNotFoundException e) {}
        return "";
    }

    /**
     * 检查应用拥有的权限
     * @param context
     *                  应用上下文
     * @param permName
     *                  权限名称（The name of the permission you are checking for）
     * @return
     */
    public static boolean checkPermission(Context context, String permName)
    {
        PackageManager localPackageManager = context.getPackageManager();
        if (localPackageManager.checkPermission(permName, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 获取应用包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context)
    {
        return context.getPackageName();
    }

    /**
     * 获取应用名称
     * @param context
     *               应用上下文
     * @return String 应用名称
     */
    public static String getAppLabel(Context context)
    {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info;
        try
        {
            info = pm.getApplicationInfo(context.getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            info = null;
        }
        String str = (String)(info != null ? pm.getApplicationLabel(info) : "");
        return str;
    }

    /**
     * 获取处理器（CPU），例如：ARMv7 processor rev 1 (v71)
     * @return 设备处理器
     */
    public static String getProcessor()
    {
        String str = null;

        FileReader fileReader = null;
        BufferedReader bufferReader = null;
        try
        {
            fileReader = new FileReader("/proc/cpuinfo");
            if (fileReader != null) {
                try
                {
                    bufferReader = new BufferedReader(fileReader, 1024);
                    str = bufferReader.readLine();
                    bufferReader.close();
                    fileReader.close();
                }
                catch (IOException e)
                {
                    Logger.w("Could not read from file /proc/cpuinfo", e);
                }
            }
            if (str == null) {
                return "";
            }
        }
        catch (FileNotFoundException e)
        {
            Logger.w("Could not open file /proc/cpuinfo", e);
        }
        int i = str.indexOf(':') + 1;
        str = str.substring(i);
        return str.trim();
    }


    /**
     * 获取mac地址
     * @param context
     * @return
     */
    public static String getMacAddr(Context context)
    {
        try
        {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, permission.ACCESS_WIFI_STATE))
            {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return wifiInfo.getMacAddress();
            }
            Logger.w("Could not get mac address.["+ permission.ACCESS_WIFI_STATE+"]");
        }
        catch (Exception e)
        {
            Logger.e("Could not get mac address." + e.toString());
        }
        return "";
    }


    /**
     * 获取设备ID，如果设备ID不存在，则使用Secure.ANDROID_ID代替
     * @param context
     * @return
     */
    public static String getDeviceId(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null)
        {
            Logger.w("No IMEI.");
        }
        String deviceId = "";
        try
        {
            if (checkPermission(context, permission.READ_PHONE_STATE))
            {
                deviceId = telephonyManager.getDeviceId();
            }
        }
        catch (Exception e)
        {
            Logger.e("No IMEI.", e);
        }
        if (TextUtils.isEmpty(deviceId))
        {
            Logger.w("No IMEI.");
            deviceId = getMacAddr(context);
            if (TextUtils.isEmpty(deviceId))
            {
                Logger.w("Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
                deviceId = Settings.Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                Logger.i("getDeviceId: Secure.ANDROID_ID: " + deviceId);
                return deviceId;
            }
        }
        return deviceId;
    }


    /**
     * 获取md5加密之后的设备ID
     * @param context
     * @return
     */
    public static String getDeviceIdMd5(Context context)
    {
        return FormatHelper.md5(getDeviceId(context));
    }

    /**
     * 获取网络运营商的名称
     * @param context
     * @return
     */
    public static String getNetworkCarrierName(Context context)
    {
        try
        {
            return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        }
        catch (Exception e)
        {
            Logger.e("read carrier fail", e);
        }
        return "Unknown";
    }


    /**
     * 获取显示器分辨率（高*宽）
     * @param context
     * @return String pixels(height*width)
     */
    public static String getResolution(Context context)
    {
        try
        {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);

            int widthPixels = dm.widthPixels;
            int heightPixels = dm.heightPixels;

            return String.valueOf(heightPixels) + "*" + String.valueOf(widthPixels);
        }
        catch (Exception e)
        {
            Logger.w("fail to get the resolution", e);
        }
        return "";
    }

    /**
     * 获取网络类型
     * @param context
     * @return String[] array-index: 0--网络类型， 1--网络类型子名称
     */
    public static String[] getNetworkType(Context context)
    {
        String[] networkTypeArr = {"", ""};
        try
        {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission.ACCESS_NETWORK_STATE, context.getPackageName()) != PackageManager.PERMISSION_GRANTED)
            {
                networkTypeArr[0] = NETWORK_TYPE_NO;
                return networkTypeArr;
            }
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
            {
                networkTypeArr[0] = NETWORK_TYPE_NO;
                return networkTypeArr;
            }
            NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info.getState() == NetworkInfo.State.CONNECTED)
            {
                networkTypeArr[0] = NETWORK_TYPE_WIFI;
                return networkTypeArr;
            }
            NetworkInfo info2 = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info2.getState() == NetworkInfo.State.CONNECTED)
            {
                networkTypeArr[0] = NETWORK_TYPE_MOBILE;
                networkTypeArr[1] = info2.getSubtypeName();
                return networkTypeArr;
            }
        }
        catch (Exception e)
        {
            Logger.w("get network fail", e);
        }
        return networkTypeArr;
    }

    /**
     * 判断网络是否已连接且为WIFI
     * @param context
     * @return
     */
    public static boolean isWifi(Context context)
    {
        return NETWORK_TYPE_WIFI.equals(getNetworkType(context)[0]);
    }

    /**
     * 判断网络是否已连接.
     * @param context
     * @return
     */
    public static boolean isConnected(Context context)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            }
            return false;
        }
        catch (Exception localException) {}
        return true;
    }

    /**
     * 判断外部存储是否已安装（SD卡）
     * @return
     */
    public static boolean isMountedForExternalStorage()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 当前设备当前时间停留在几点钟
     * @param context
     * @return int 本地时区几点钟，如果失败则判为早上8点钟
     */
    public static int getLocaleHour(Context context)
    {
        try
        {
            Locale locale = getLocale(context);
            Calendar calendar = Calendar.getInstance(locale);
            if (calendar != null) {
                return calendar.getTimeZone().getRawOffset() / 3600000;
            }
        }
        catch (Exception e)
        {
            Logger.w("error in getTimeZone", e);
        }
        return DEFAULT_LOCALE_HOUR;
    }

    /**
     * 获取本地信息
     * @param context
     * @return String[] index: 0--国家；1--语言
     */
    public static String[] getLocaleInfo(Context context)
    {
        String[] localeInfo = new String[2];
        try
        {
            Locale locale = getLocale(context);
            if (locale != null)
            {
                localeInfo[0] = locale.getCountry();
                localeInfo[1] = locale.getLanguage();
            }
            if (TextUtils.isEmpty(localeInfo[0])) {
                localeInfo[0] = "Unknown";
            }
            if (TextUtils.isEmpty(localeInfo[1])) {
                localeInfo[1] = "Unknown";
            }
            return localeInfo;
        }
        catch (Exception e)
        {
            Logger.w("error in getLocaleInfo", e);
        }
        return localeInfo;
    }

    /**
     * 获取用户偏好的本地化设置
     * @param context
     * @return Locale Current user preference for the locale
     */
    private static Locale getLocale(Context context)
    {
        Locale locale = null;
        try
        {
            Configuration config = new Configuration();
            config.setToDefaults();
            Settings.System.getConfiguration(context.getContentResolver(), config);
            if (config != null) {
                locale = config.locale;
            }
        }
        catch (Exception localException)
        {
            Logger.w("fail to read user config locale");
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 获取应用标识--appkey（即AndroidManifest.xml文件中配置的name=UMENG_APPKEY的metaData的value）
     * @param context
     *                 应用上下文
     * @return appkey  应用标识
     */
    public static String getMetaAppkey(Context context)
    {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if ((appInfo != null) && (appInfo.metaData != null))
            {
                String appkey = appInfo.metaData.getString("UMENG_APPKEY");
                if (appkey == null)
                {
                    Logger.e("Could not read JJ_APPKEY meta-data from AndroidManifest.xml.");
                }
                return appkey.toString();
            }
        } catch (Exception e) {
            Logger.e("Could not read JJ_APPKEY meta-data from AndroidManifest.xml.", e);
        }
        return null;
    }

    /**
     * 获取配置的应用渠道标识（即AndroidManifest.xml文件中配置的name=UMENG_CHANNEL的metaData的value）
     * @param context
     *                应用上下文
     * @return
     */
    public static String getMetaChannel(Context context)
    {
        String channel = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        try
        {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if ((appInfo != null) && (appInfo.metaData != null))
            {
                Object metaChannel = appInfo.metaData.get("UMENG_CHANNEL");
                if (metaChannel == null)
                {
                    Logger.w("Could not read UMENG_CHANNEL meta-data from AndroidManifest.xml.");
                } else {
                    if (metaChannel instanceof String){
                        channel = (String)metaChannel;
                    } else if (metaChannel instanceof Integer){
                        channel = String.valueOf(metaChannel);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Logger.e("Could not read JJ_CHANNEL meta-data from AndroidManifest.xml.", e);
        }
        return channel;
    }


    /**
     * 获取本机的ip
     * @return
     */
    public static String getLocalHostIp(){
        String ipaddress = "";
        try{
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()){
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()){
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()){
                        return ip.getHostAddress();
                    }
                }
            }
        }catch (SocketException e){
            Logger.e("Could not read ip address.", e);
        }
        return ipaddress;
    }

}