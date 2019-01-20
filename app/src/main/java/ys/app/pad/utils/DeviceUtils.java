/**
 * Copyright ® 2007-2014 ebrun.com Co. Ltd.
 * All right reserved.
 */
package ys.app.pad.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtils 
{
	public static final int SDK_VERSION_1_5 = 3;
	public static final int SDK_VERSION_1_6 = 4;
	public static final int SDK_VERSION_2_0 = 5;
	public static final int SDK_VERSION_2_0_1 = 6;
	public static final int SDK_VERSION_2_1 = 7;
	public static final int SDK_VERSION_2_2 = 8;
	public static final int SDK_VERSION_2_3 = 9;
	public static final int SDK_VERSION_2_3_3 = 10;
	public static final int SDK_VERSION_3_0 = 11;

	public static String getDeviceModel()
	{
		return Build.MODEL;
	}

	public static String getIMEI(Context context)
	{
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public static String getIMSI(Context context)
	{
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}

	public static String getSDKVersion()
	{
		return Build.VERSION.CODENAME;
	}

	public static int getSDKVersionInt()
	{
		return Build.VERSION.SDK_INT;
	}

	public static float getScreenDensity(Context context)
	{
		return getDisplayMetrics(context).density;
	}

	public static int getScreenDensityDpi(Context context)
	{
		return getDisplayMetrics(context).densityDpi;
	}

	public static Rect getScreenRect(Context context)
	{
		Display localDisplay = getDefaultDisplay(context);
		int i = localDisplay.getWidth();
		int j = localDisplay.getHeight();
		return new Rect(0, 0, i, j);
	}

	public static int getScreenWidth(Context context)
	{
		Display localDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return localDisplay.getWidth();
	}

	public static int getScreenHeight(Context context)
	{
		return getDefaultDisplay(context).getHeight();
	}
	
	public static int getScreenWidthPixels(Context context)
	{
		return getDisplayMetrics(context).widthPixels;
	}
	
	public static int getScreenHeightPixels(Context context)
	{
		return getDisplayMetrics(context).heightPixels;
	}
	
	public static Display getDefaultDisplay(Context context)
	{
		return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}
	
	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		return context.getApplicationContext().getResources().getDisplayMetrics();
	}

	public static String getDeiceUUID(Activity mActivity) {

		return new DeviceUuidFactory(mActivity).getDeviceUuid().toString();

	}
}