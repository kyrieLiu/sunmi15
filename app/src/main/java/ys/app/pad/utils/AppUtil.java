package ys.app.pad.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ys.app.pad.model.ServiceTypeInfo;

/**
 * Created by aaa on 2017/3/24.
 */

public class AppUtil {


    private static NumberFormat nf=new DecimalFormat("#,###.00");
    private static SimpleDateFormat dayFormat =new SimpleDateFormat("yyyy-MM-dd");


    public static String getOrderNum() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String time=sdFormat.format(new Date());
        return time;
    }

    public static String getIds(List<ServiceTypeInfo> list) {
        if (list != null && list.size() != 0) {
            StringBuffer buffer = new StringBuffer();
            for (ServiceTypeInfo info : list) {
                if (info.getCardType() == 1) {
                    buffer.append(info.getId()).append(",");
                }
            }
            String result = "," + buffer.toString();
            return result;
        }
        return null;
    }

    //    1001	现金
//    1003	微信
//    1004	支付宝
//    1005	百度钱包
//    1006	银行卡
//    1007	易付宝
//    1009	京东钱包
//    1011	QQ钱包
    public static String getPayType(String pay_type) {
        if (StringUtils.isEmptyOrWhitespace(pay_type)) {
            return "现金";
        }
        String type="现金";
        switch (pay_type){
            case "1001":
                type= "现金";
                break;
            case "1006":
                type= "银行卡";
                break;
            case "8888":
                type= "会员卡";
                break;
            case "0012":
                type= "微信";
                break;
            case "0013":
                type= "支付宝";
                break;
            case "0112":
                type= "个人微信";
            break;
            case "0113":
                type= "个人支付宝";
            break;
            case "9999":
                type="次卡";
                break;
            default:
                type= "现金";
                break;
        }
        return type;
    }


    public static String getOrderNoAppendRandom(String orderNo) {
        return orderNo + "_" + new Random().nextInt(1000);
    }

    public static String getOrderNoCutRandom(String out_trade_no) {
        if (out_trade_no.contains("_")) {
            String[] split = out_trade_no.split("_");
            out_trade_no = split[0];
        }
        return out_trade_no;
    }


    public static String getTimes(long time) {
        String times = "";
        try {
            times = longToString(time, "yyyy-MM-dd HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getDayTimes(long time) {
        String times = "";
        try {
            times = longToString(time, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }
    //将long型转化为标准时间字符串
    public static String longToStandardTime(long time){
        if (time!=0){
            Date date=new Date(time);
            String standardTime= dayFormat.format(date);
            return standardTime;
        }else{
            return "";
        }
    }

    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 判断充值是否成功
     *
     * @param state
     * @return
     */
    public static boolean isPaySuc(String state) {
        boolean isPaySuc = false;
        if (TextUtils.isEmpty(state)) {
            isPaySuc = false;
        } else if ("1".equals(state)) {
            isPaySuc = true;
        }
        return isPaySuc;
    }


    /**
     * 得到是几位小数
     *
     * @param inStr
     * @return
     */
    public static int getPointIndex(String inStr) {
        int numOfBits = -1;
        if (TextUtils.isEmpty(inStr)) {
            return numOfBits;
        }
        //正则表达式判断是否为小数
        Pattern p = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");
        Matcher m = p.matcher(inStr);
        boolean b = m.matches();
        if (b) {//是小数则判断位数
            //获取小数点的位置
            int bitPos = inStr.indexOf(".");
            //字符串总长度减去小数点位置，再减去1，就是小数位数
            numOfBits = inStr.length() - bitPos - 1;
        } else {//不是小数，给出错误提示信息

        }
        return numOfBits;
    }

    /**
     * 得到一个整数的折扣数
     *
     * @param discount 折扣数
     * @return 返回整数
     */
    public static String discountShow(double discount) {
        String str = "";
        String discountStr = String.valueOf(discount);
        int pointIndex = AppUtil.getPointIndex(discountStr);
        if (pointIndex == 1) {
            str = String.valueOf((float)discount * 10).substring(0, 1);
        } else if (pointIndex == 2) {
            str = String.valueOf((float)discount * 100).substring(0, 2);
        }
        return str;
    }

    /**
     * 得到一个整数的折扣数
     *
     * @param discount 折扣数
     * @return 返回整数
     */
    public static String noDiscountShow(double discount) {
        String str = "";
        if (discount<=1){
            String discountStr = String.valueOf(discount);
            int pointIndex = AppUtil.getPointIndex(discountStr);
            Logger.e("pointIndex = " + pointIndex);
            if (pointIndex == 1) {
                str = String.valueOf((float)discount * 10).substring(0, 1);
                if (discount==0.1){
                    str="1";
                }else if ("1".endsWith(str)) {
                    str = "不打";
                }
            } else if (pointIndex == 2) {
                str = String.valueOf((float)discount * 100).substring(0, 2);
                if ("10".endsWith(str)) {
                    str = "不打";
                }
            }
        }else{
            return  String.valueOf(discount*10);
        }
        return str;
    }


    /**
     * @param discount 传入折扣
     * @return
     */
    public static String uploadDiscountStr(String discount) {
        String str = "";
        if (TextUtils.isEmpty(discount))
            return str;

        if (discount.length() == 1) {
            try {
                DecimalFormat df = new DecimalFormat("0.0");
                str = df.format(Integer.parseInt(discount) / 10f);
            } catch (NumberFormatException e) {

            }
        } else if (discount.length() == 2) {
            try {
                if ("10".equals(discount)||"不打".equals(discount)){
                    return "1";
                }
                String lastStr = discount.substring(1, 2);
                if ("0".equals(lastStr)) {//最后一位是0
                    String firstStr = discount.substring(0, 1);
                    DecimalFormat df = new DecimalFormat("0.0");
                    str = df.format(Integer.parseInt(firstStr) / 10f);
                } else {//最后一位不是0
                    DecimalFormat df = new DecimalFormat("0.00");
                    str = df.format(Integer.parseInt(discount) / 100f);
                }
            } catch (NumberFormatException e) {

            }
        }
        return str;

    }

    public static String costParams(int cost) {
        if (cost < 10) {
            return ((double) cost) / 10.00 + "";
        } else {
            return ((double) cost) / 100.00 + "";
        }

    }

    public static boolean getListSizeIsEmpty(List list) {
        if (list == null) return true;
        if (0 == list.size()) return true;
        return false;
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
    //将金额转化为标准格式
    public static String formatStandardMoney(String t){
        t = t.replaceAll(",","");
        String str;
        if (t!=null){
            double d=Double.parseDouble(t);
            if (d>=1||d<=-1){
                str = nf.format(d);
            }else if (d>-1&&d<0){
                d=0-d;
                str="-0"+nf.format(d);
            }else{
                str="0"+nf.format(d);
            }
        } else{
            str="0";
        }

        return  str;
    }
    public static double getRealMoney(int vipUserId,int isPromotion,int isFold,double countVipMoney,double countPromotionMoney,double countMoney){
        double realMoney=0;
        if (vipUserId > 0) {//会员卡
            if (1 == isPromotion) {
                if (1 == isFold) {//折上折
                    realMoney=countVipMoney;
                } else {
                    realMoney=countPromotionMoney;
                }
            } else {
                realMoney=countVipMoney;
            }
        } else {
            if (1 == isPromotion) {
                realMoney = countPromotionMoney;
                //view.setText("优惠后总价：￥" +AppUtil.formatStandardMoney(countPromotionMoney));
            } else {
                realMoney = countMoney;
                //view.setText("总价：￥" + AppUtil.formatStandardMoney(countMoney));
            }
        }
        return realMoney;
    }



}
