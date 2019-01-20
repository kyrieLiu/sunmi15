package ys.app.pad.shangmi.t1miniscan;

import android.graphics.Point;


/**
 * Created by KenMa on 2016/10/10.
 */
public class ScanConfig {
    //当前分辨率
     public static int CURRENT_PPI = 1;
    public static final int PPI_160_120 = 2;
    public static final int PPI_176_144 = 3;
    public static final int PPI_320_240 = 4;
    public static final int PPI_352_288 = 5;;
    //扫描完成声音提示
    public static boolean PLAY_SOUND = true;
    //扫描完成震动
    public static boolean PLAY_VIBRATE = false;
    //识别反色二维码
    public static boolean IDENTIFY_INVERSE_QR_CODE = true;
    //识别画面中多个二维码
    public static boolean IDENTIFY_MORE_CODE = false;
    //是否显示设置按钮
    public static boolean IS_SHOW_SETTING = true;
    //是否显示选择相册按钮
    public static boolean IS_SHOW_ALBUM = true;

    //灯模式: false 灯灭; true 灯亮
    public static boolean IS_OPEN_LIGHT = false;

    //灯亮时间（单位: 毫秒）
    public static int LIGHT_BRIGHT_TIME = 200;

    //灯灭时间（单位: 毫秒）
    public static int LIGHT_DROWN_TIME = 500;

    //扫码模式: false 单次扫码; true 循环扫码
    public static boolean SCAN_MODE = false;

    //灯索引: 0 nfc灯; 1 camera灯; 其它错误
    public static int LIGHT_INDEX = 1;

    //扫描结果的数据的键
    public static final String TYPE = "TYPE";//扫描码的类型
    public static final String VALUE = "VALUE";//扫描码的数据

    public static Point BEST_RESOLUTION ;
}
