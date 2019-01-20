package ys.app.pad.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：lv
 * 时间：2017/3/26 11:32
 */

public class DateUtil {

    /**
     * long转date "yyyy-MM-dd"
     */
    public static String longFormatDate(long date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(date);
        String dateString = df.format(d);
        return dateString;
    }

    public static String longFormatDate(String time) {
        if (time.length() > 9) {
            time = time.substring(0, 10);
        }
        return time;
    }



    /**
     * long转date "yyyy-MM-dd HH:mm"
     */
    public static String longFormatDate2(long date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(date);
        String dateString = df.format(d);
        return dateString;
    }


    /**
     * long转date "yyyy-MM-dd HH:mm:ss"
     */
    public static String longFormatDate3(long date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(date);
        String dateString = df.format(d);
        return dateString;
    }


    /**
     * long转time "HH:mm"
     */
    public static String longFormatTime2(long date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Date d = new Date(date);
        String dateString = df.format(d);
        return dateString;
    }

    public static String getPreviousSixMonth(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -6);
        Date m3 = c.getTime();
        return format.format(m3);
    }
    public static String getThisMonthFirstDayYMD() {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        String dateString = df.format(new Date().getTime());
        return dateString + "-01";
    }
    public static String getTodayYMD() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = df.format(new Date().getTime());
        return dateString;
    }
}
