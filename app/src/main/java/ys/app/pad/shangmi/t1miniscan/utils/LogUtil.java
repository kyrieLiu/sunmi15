package ys.app.pad.shangmi.t1miniscan.utils;

import android.text.TextUtils;
import android.util.Log;

import ys.app.pad.BuildConfig;


/**
 * 打印Log
 *
 * @author shenzhou
 *         2014年12月17日
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void v(String TAG, String msg) {
        if (IS_DEBUG && !TextUtils.isEmpty(msg)) {
            MyLog(VERBOSE, TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (IS_DEBUG && !TextUtils.isEmpty(msg)) {
            MyLog(DEBUG, TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (IS_DEBUG && !TextUtils.isEmpty(msg)) {
            MyLog(INFO, TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (IS_DEBUG && !TextUtils.isEmpty(msg)) {
            MyLog(WARN, TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (IS_DEBUG && !TextUtils.isEmpty(msg)) {
            MyLog(ERROR, TAG, msg);
        }
    }

    private static void MyLog(int type, String TAG, String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");
        stringBuilder.append(msg);
        String logStr = stringBuilder.toString();
        switch (type) {
            case VERBOSE:
                Log.v(TAG, logStr);
                break;
            case DEBUG:
                Log.d(TAG, logStr);
                break;
            case INFO:
                Log.i(TAG, logStr);
                break;
            case WARN:
                Log.w(TAG, logStr);
                break;
            case ERROR:
                Log.e(TAG, logStr);
                break;
            default:
                break;
        }
    }
}
