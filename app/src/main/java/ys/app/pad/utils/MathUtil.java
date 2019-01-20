package ys.app.pad.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MathUtil {
    private static NumberFormat nf=new DecimalFormat("#,###.00");
    public static double add(double d1,double d2,double d3){
        return d1+d2+d3;
    }

    //保留两位小数 四舍五入
    public static Double retainTwoDecimal(Double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        String formatD=df.format(d);
        double fd=Double.parseDouble(formatD);
        return fd;
    }
    //保留两位小数 四舍五入
    public static Double retainTwoDecimal1(Double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(d));
    }
    //将金额转化为标准格式
    public static String formatStandardMoney(double d){
        String str;
        if (d>=1||d<=-1){
            str = nf.format(d);
        }else if (d>-1&&d<0){
            d=0-d;
            str="-0"+nf.format(d);
        }else{
            str="0"+nf.format(d);
        }
        return  str;
    }
}
