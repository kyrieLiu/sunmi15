package ys.app.pad.shangmi.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.itemmodel.DailySettlementBean;
import ys.app.pad.model.ChargeResultInfo;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.shangmi.printer.gputil.DeviceConnFactoryManager;
import ys.app.pad.shangmi.printer.gputil.PrinterCommand;
import ys.app.pad.shangmi.printer.gputil.ThreadPool;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static ys.app.pad.shangmi.printer.gputil.Constant.ACTION_USB_PERMISSION;
import static ys.app.pad.shangmi.printer.gputil.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static ys.app.pad.shangmi.printer.gputil.DeviceConnFactoryManager.CONN_STATE_FAILED;


public class BlueToothPrintUtil {


    private static BlueToothPrintUtil mAidlUtil = new BlueToothPrintUtil() {
    };
    private Context context;

    private String mediumSpline;
    private int largeSize, row1, row2, row3, contentSize, titleSize, lineNumber,orderRow1,orderRow2,orderRow3;

    private static final String TAG = "AidlUtil";

    private BluetoothSocket mSocket;

    private PrintUtil printUtil;

    private OutputStream mOutputStream = null;
    private int id = 0;

    private DeviceConnFactoryManager deviceManager;
    private ThreadPool threadPool;


    private BlueToothPrintUtil() {
        largeSize = 14 * 2;
        mediumSpline = "-------------------------------";
        orderRow1 = 11;
        orderRow2 = 4;
        orderRow3 = 6;
        row1 = 18;
        row2 = 5;
        row3 = 8;
        titleSize = 35;
        contentSize = 24;
        lineNumber = 7;
    }

    public static BlueToothPrintUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context;

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        this.context.registerReceiver(receiver, filter);

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (final BluetoothDevice device : pairedDevices) {
                int deviceType = device.getBluetoothClass().getMajorDeviceClass();
                Log.i("info", "已建立配对的设备类型" + deviceType + "名称==" + device.getName() + "  ID==" + device.getAddress());
                String deviceKey=device.getName().substring(0,7);

                if ("Printer".equals(deviceKey)) {
                            new DeviceConnFactoryManager.Build()
                                    .setId(0)
                                    //设置连接方式
                                    .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                                    //设置连接的蓝牙mac地址
                                    .setMacAddress(device.getAddress())
                                    .build();
                            //打开端口
                            deviceManager =DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];

                            if (deviceManager !=null) deviceManager.openPort();
                        }
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                System.out.println("permission ok for device " + device);
                                //usbConn(device);
                            }
                        } else {
                            System.out.println("permission denied for device " + device);
                        }
                    }
                    break;
                //Usb连接断开、蓝牙连接断开广播
                case ACTION_USB_DEVICE_DETACHED:
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (id == deviceId) {
                                Logger.d("连接状态：未连接");
                                //tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            Logger.d("连接状态：连接中");
                            //tvConnState.setText(getString(R.string.str_conn_state_connecting));
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            Logger.d("连接状态：已连接");
                            Toast.makeText(context,"蓝牙打印机连接成功",Toast.LENGTH_SHORT).show();
                            //tvConnState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                            break;
                        case CONN_STATE_FAILED:
                            Logger.d("连接状态：连接失败");
                            Toast.makeText(context,"蓝牙打印机连接失败,请检查蓝牙连接",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    break;
                case ACTION_QUERY_PRINTER_STATE:
//                    if(counts >0) {
//                        sendContinuityPrint();
//                    }
                    break;
                default:
                    break;
            }
        }
    };



    public void printLabel(Bitmap b){
        if(deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.ESC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(55, 30);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(1);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();

        // 绘制图片
        //tsc.addBitmap(20, 50, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);

        // 绘制简体中文
        tsc.addBitmap(50, 25, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
        String title="卡兹乐天然狗罐金枪鱼+鱼片";
        String price="价格: 22.30";
        int sizeTitle = largeSize - length(title);
        // 文字居中需要在前面补足相应空格，后面可以用换行符换行
        String titleStr = getBlankBySize((int) (sizeTitle / 2d)) + title;
        int priceSize=largeSize-length(price);
        String priceStr=getBlankBySize((int) (priceSize / 2d)) + price;
        // 绘制简体中文
        tsc.addText(60, 162, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                titleStr);
        tsc.addText(60, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                priceStr);
        //tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        //tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        // 打印标签份数
        tsc.addPrint(1);
        // 打印标签后 蜂鸣器响

        //tsc.addSound(2, 100);
        //tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (deviceManager == null) {
            return;
        }
        deviceManager.sendDataImmediately(datas);
    }


    /**
     * 发送票据
     */
    public void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("Sample\n");
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        // 打印文字
        esc.addText("Print text\n");
        // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n");

		/* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

		/* 打印图片 */
        // 打印文字
        esc.addText("Print bitmap!\n");
        // 打印图片
        //esc.addOriginRastBitImage(b, 384, 0);

		/* 打印一维条码 */
        // 打印文字
        esc.addText("Print code128\n");
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
        // 设置条码可识别字符位置在条码下方
        // 设置条码高度为60点
        esc.addSetBarcodeHeight((byte) 60);
        // 设置条码单元宽度为1
        esc.addSetBarcodeWidth((byte) 1);
        // 打印Code128码
        esc.addCODE128(esc.genCodeB("SMARNET"));
        esc.addPrintAndLineFeed();

		/*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
        // 打印文字
        esc.addText("Print QRcode\n");
        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31);
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode((byte) 3);
        // 设置qrcode内容
        esc.addStoreQRCodeData("www.smarnet.cc");
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        //打印文字
        esc.addText("Completed!\r\n");

        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }




    /**
     * 打印次卡支付小票
     */


    public void printNumOrderPayInformation(OrderInfo mOrderInfo) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        List<LoginInfo> loginInfos = GreenDaoUtils.getmDaoSession().getLoginInfoDao().loadAll();
        LoginInfo loginInfo = null;
        if (loginInfos != null && loginInfos.size() > 0) {
            loginInfo = loginInfos.get(0);
        }
        String title = SpUtil.getShopName();
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(title+"\n\n");
        StringBuilder sbTable = new StringBuilder();
        String orderNo = "订单号：" + mOrderInfo.getOrderNo();
        sbTable.append(orderNo + "\n");
        String transactionTime = "交易时间：" + DateUtil.longFormatDate2(mOrderInfo.getOrderTime());
        sbTable.append(transactionTime + "\n");

        if (loginInfo != null) {
            String phone = "";
            if (loginInfo.getPhone() != null) {
                phone = loginInfo.getPhone();
            }
            String contact = "联系电话：" + phone;
            sbTable.append(contact + "\n");
        }

        int totalCount = 0;
        String headerRow1 = "商品名称";
        String headerRow2 = "数量";
        List<OrderInfo.OrderDetailedListBean> orderDetailedList = mOrderInfo.getOrderDetailedList();
        if (orderDetailedList != null && !orderDetailedList.isEmpty()) {
            List<String> userList = new ArrayList<>();
            for (OrderInfo.OrderDetailedListBean orderBean : orderDetailedList) {
                if (!userList.contains(orderBean.getUserName())) {
                    userList.add(orderBean.getUserName());
                }
            }
            StringBuilder userBuilder = new StringBuilder();
            for (String userName : userList) {
                userBuilder.append(userName + " ");
            }
            String receiveEmployee = "收款员工：" + userBuilder.toString();
            sbTable.append(receiveEmployee + "\n");

            if (loginInfo != null) {
                String address = loginInfo.getAddress();
                String addressInformation = "地址：" + address;
                sbTable.append(addressInformation + "\n");
            }
            sbTable.append(mediumSpline + "\n");
            String str1 = headerRow1 + getBlankBySize((int) (row1 -
                    length(headerRow1)));
            String str2 = getBlankBySize((int) (row3 - length(headerRow2))) + headerRow2;

            String headerStr = str1 + str2;
            sbTable.append(headerStr + "\n");
            sbTable.append(mediumSpline + "\n");
            for (int i = 0; i < orderDetailedList.size(); i++) {
                OrderInfo.OrderDetailedListBean info = orderDetailedList.get(i);
                double nameSize = length(info.getName());
                if (nameSize > row1) {
                    sbTable.append(info.getName() + "\n");
                    String newLineSecond = getBlankBySize(row3 - length(info.getCount() + "")) + info.getCount();
                    String newLineAll = newLineSecond + "\n";
                    sbTable.append(getBlankBySize(row1) + newLineAll);
                } else {
                    // 正常
                    String rowFirst = info.getName() + getBlankBySize(row1 - length(info.getName()));
                    String rowSecond = getBlankBySize(row3 - length(info.getCount() + "")) + info.getCount();
                    sbTable.append(rowFirst + rowSecond + "\n");
                }
                totalCount = totalCount + info.getCount();
            }
            sbTable.append(mediumSpline + "\n");
            sbTable.append("商品总数：" + totalCount + "件\n");
            sbTable.append(mediumSpline + "\n");

        }
        String cardNo = "会员卡号：" + mOrderInfo.getVipCardNo();
        sbTable.append(cardNo + "\n");
        String phone = "会员电话：" + StringUtils.hihtPhone(mOrderInfo.getVipPhone());
        sbTable.append(phone + "\n");
        List<OrderInfo.OrderDetailedListBean> list = mOrderInfo.getOrderDetailedList();
        Map<String, List<OrderInfo.OrderDetailedListBean>> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            List<OrderInfo.OrderDetailedListBean> filterList = new ArrayList<>();
            OrderInfo.OrderDetailedListBean iBean = list.get(i);
            String name = iBean.getName();
            for (int j = 1; j < list.size(); j++) {
                OrderInfo.OrderDetailedListBean jBean = list.get(j);
                if (name.equals(jBean.getName())) {
                    filterList.add(jBean);
                    list.remove(jBean);
                }
            }
            filterList.add(iBean);
            list.remove(iBean);
            i--;
            map.put(name, filterList);
        }
        Iterator<Map.Entry<String, List<OrderInfo.OrderDetailedListBean>>> iterator = map.entrySet().iterator();
        StringBuilder payInfo = new StringBuilder();
        StringBuilder beforeInfo = new StringBuilder();
        StringBuilder afterInfo = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, List<OrderInfo.OrderDetailedListBean>> entry = iterator.next();
            String name = entry.getKey();
            List<OrderInfo.OrderDetailedListBean> itemList = entry.getValue();
            int beforNum = -1, afterNum = 10000000;
            for (OrderInfo.OrderDetailedListBean itemBean : itemList) {
                if (beforNum < itemBean.getBeforeNum()) {
                    beforNum = itemBean.getBeforeNum();
                }
                if (afterNum > itemBean.getAfterNum()) {
                    afterNum = itemBean.getAfterNum();
                }
            }
            beforeInfo.append(" " + name + beforNum + "次");
            afterInfo.append(" " + name + afterNum + "次");
            payInfo.append(" " + name + (beforNum - afterNum) + "次");
        }
        String jine = "支付前该服务剩余次数:" + beforeInfo.toString();
        sbTable.append(jine + "\n");
        String paymentAmountMonkey = "次卡支付:" + payInfo.toString();
        sbTable.append(paymentAmountMonkey + "\n");
        String yue = "支付后该服务剩余次数:" + afterInfo.toString();
        sbTable.append(yue);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText(sbTable.toString()+"\n\n\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);
    }


    /**
     * 打印订单小票
     *
     * @param mOrderInfo
     */
    public void printOrderPayInformation(OrderInfo mOrderInfo) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        if (SpUtil.getReceiptPrinterVip()){
            orderRow1 = 11;
            orderRow2 = 4;
            orderRow3 = 6;
        }else{
            orderRow1 = 18;
            orderRow2 = 5;
            orderRow3 = 8;
        }

        List<LoginInfo> loginInfos = GreenDaoUtils.getmDaoSession().getLoginInfoDao().loadAll();
        LoginInfo loginInfo = null;
        if (loginInfos != null && loginInfos.size() > 0) {
            loginInfo = loginInfos.get(0);
        }

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        //esc.addSetLeftMargin((short) 30);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        String title = SpUtil.getShopName();
        esc.addText(title+"\n\n");
        StringBuilder sbTable = new StringBuilder();
        String orderNo = "订单号：" + mOrderInfo.getOrderNo();
        sbTable.append(orderNo + "\n");
        String transactionTime = "交易时间：" + DateUtil.longFormatDate2(mOrderInfo.getOrderTime());
        sbTable.append(transactionTime + "\n");

        if (loginInfo != null) {
            String phone = "";
            if (loginInfo.getPhone() != null) {
                phone = loginInfo.getPhone();
            }
            String contact = "联系电话：" + phone;
            sbTable.append(contact + "\n");
        }

        int totalCount = 0;
        String headerRow1 = "商品名称";
        String headerRow2 = "数量";
        String headerRow3 = "会员价";
        String headerRow4 = "售价";
        List<OrderInfo.OrderDetailedListBean> orderDetailedList = mOrderInfo.getOrderDetailedList();
        if (orderDetailedList != null && !orderDetailedList.isEmpty()) {
            List<String> userList = new ArrayList<>();
            for (OrderInfo.OrderDetailedListBean orderBean : orderDetailedList) {
                if (!userList.contains(orderBean.getUserName())) {
                    userList.add(orderBean.getUserName());
                }
            }
            StringBuilder userBuilder = new StringBuilder();
            for (String userName : userList) {
                userBuilder.append(userName + " ");
            }
            String receiveEmployee = "收款员工：" + userBuilder.toString();
            sbTable.append(receiveEmployee + "\n");

            if (loginInfo != null) {
                String address = loginInfo.getAddress();
                String addressInformation = "地址：" + address;
                sbTable.append(addressInformation + "\n");
            }
            sbTable.append(mediumSpline + "\n");
            String str1 = headerRow1 + getBlankBySize(orderRow1 - 1  - length(headerRow1));
            String str2 = headerRow2 + getBlankBySize(orderRow2 + 2 - length(headerRow2));
            String str3="";
            if (SpUtil.getReceiptPrinterVip()){
                str3 = headerRow3+ getBlankBySize(orderRow3 +2  - length(headerRow3));
            }
            String str4 = headerRow4;

            String headerStr = str1 + str2 + str3+str4;
            sbTable.append(headerStr + "\n");
            sbTable.append(mediumSpline + "\n");
            for (OrderInfo.OrderDetailedListBean bean : orderDetailedList) {
                String count = bean.getCount() + "";
                String name = bean.getName();
                String price = "";
                String vipPrice="￥" + AppUtil.formatStandardMoney(bean.getVipAmt());
                if (1 == bean.getIsGift()) {
                    price = "(赠品)";
                } else {
                    price = "￥" + AppUtil.formatStandardMoney(bean.getRealMoney());
                }
                totalCount = totalCount + bean.getCount();
                double nameSize = length(name);
                if (nameSize > orderRow1) {
                    // 列内容长度大于最大列长度,当成一行内容（换行）
                    sbTable.append(name + "\n");
                    // 数量和价格不会超过最大列宽，就不判断内容是否超出了
                    String newLineSecond = count + getBlankBySize(orderRow2 - length(count));
                    String newLineThird="";
                    if (SpUtil.getReceiptPrinterVip()){
                        newLineThird = vipPrice + getBlankBySize(orderRow3 - length(vipPrice));
                    }
                    String newLineEnd = " "+price + "\n";
                    String newLineAll = newLineSecond+newLineThird + newLineEnd;
                    // 左边补足row1长度空格
                    sbTable.append(getBlankBySize(orderRow1) + newLineAll);
                } else {
                    // 正常
                    String rowFirst = name + getBlankBySize(orderRow1  - length(name));
                    String rowSecond = count + getBlankBySize((int) (orderRow2 - length(count)));
                    String rowThird="";
                    if (SpUtil.getReceiptPrinterVip()){
                        rowThird = vipPrice + getBlankBySize(orderRow3 - length(vipPrice));
                    }
                    // 最后直接换行就可以了
                    String rowEnd = " "+price + "\n";
                    sbTable.append(rowFirst + rowSecond +rowThird+ rowEnd);
                }
            }
            sbTable.append(mediumSpline + "\n");
            sbTable.append("商品总数：" + totalCount + "件\n");
            sbTable.append("消费总金额：" + AppUtil.formatStandardMoney(mOrderInfo.getRealAmt()) + "元\n");
            sbTable.append(mediumSpline + "\n");
        }
        if (mOrderInfo.getVipUserId() > 0) {
            String cardNo = "会员名称：" + mOrderInfo.getVipName();
            sbTable.append(cardNo + "\n");
            String phone = "会员电话：" + StringUtils.hihtPhone(mOrderInfo.getVipPhone());
            sbTable.append(phone + "\n");
            if (mOrderInfo.getFavouredAmt()>0){
                String favorable = "本次会员优惠金额：" + AppUtil.formatStandardMoney(mOrderInfo.getFavouredAmt()) + "元";
                sbTable.append(favorable + "\n");
            }
        }else if (mOrderInfo.getFavouredAmt()>0){
            String favorable = "本次实际优惠金额：" + AppUtil.formatStandardMoney(mOrderInfo.getFavouredAmt()) + "元";
            sbTable.append(favorable + "\n");
        }
        String paymentAmountMonkey = "实付金额：" + mOrderInfo.getRealAmt() + "元";
        sbTable.append(paymentAmountMonkey + "\n");
        if (mOrderInfo.getVipUserId() > 0) {
            String yue = "支付后余额：" + AppUtil.formatStandardMoney(mOrderInfo.getAfterAmt()) + "元";
            sbTable.append(yue + "\n");
        }

        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        // 打印文字
        esc.addText(sbTable.toString()+"\n\n\n\n");
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        deviceManager.sendDataImmediately(datas);

    }

    /**
     * 打印充值小票
     *
     * @param chargeInfo
     */
    public void printChargePayInformation(ChargeResultInfo chargeInfo, ChargeResultInfo.OrderDetailedListBean chargeDetail) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        List<LoginInfo> loginInfos = GreenDaoUtils.getmDaoSession().getLoginInfoDao().loadAll();
        LoginInfo loginInfo = null;
        if (loginInfos != null && loginInfos.size() > 0) {
            loginInfo = loginInfos.get(0);
        }
        String title;
        if (chargeInfo.getIsClassification() == 1) title = "次卡充值";
        else title = "会员充值";

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(title+"\n\n");
        StringBuilder sbTable = new StringBuilder();
        String shopName = "商家名称：" + SpUtil.getShopName();
        sbTable.append(shopName + "\n");
        String userName = chargeInfo.getRechargeList().get(0).getUserName();
        if (userName != null) {
            sbTable.append("收款人员: " + userName + "\n");
        }
        String orderNo = "订单号：" + chargeInfo.getOrderNo();
        sbTable.append(orderNo + "\n");
        if (loginInfo != null) {
            String phone = "";
            if (loginInfo.getPhone() != null) {
                phone = loginInfo.getPhone();
            }
            String contact = "联系电话：" + phone;
            sbTable.append(contact + "\n");
        }
        sbTable.append(mediumSpline + "\n");

        String cardNo = "会员卡号：" + chargeInfo.getVipCardNo();
        sbTable.append(cardNo + "\n");
        String phone = "会员电话：" + StringUtils.hihtPhone(chargeInfo.getVipPhone());
        sbTable.append(phone + "\n");
        String name = "会员姓名：" + chargeInfo.getVipName();
        sbTable.append(name + "\n");
        String time = "充值时间：" + DateUtil.longFormatDate2(chargeInfo.getPayedTime()) + "\n";
        sbTable.append(time + mediumSpline + "\n");
        String beforeAmt = "充值前金额：" + AppUtil.formatStandardMoney(chargeInfo.getBeforeAmt());
        sbTable.append(beforeAmt + "\n");
        String realAmt = "充值金额：" + AppUtil.formatStandardMoney(chargeInfo.getRealAmt());
        sbTable.append(realAmt + "\n");
        double presentAmt = chargeDetail.getPresentAmt();
        if (presentAmt > 0) {
            String presentMoney = "赠送金额：" + AppUtil.formatStandardMoney(presentAmt);
            sbTable.append(presentMoney + "\n");
        }
        String afterAmt = "充值后余额：" + AppUtil.formatStandardMoney(chargeInfo.getAfterAmt());
        sbTable.append(afterAmt);

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText(sbTable.toString()+"\n\n\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);
    }

    /**
     * 打印单件退款单
     *
     * @param orderInfo
     */
    public void printRefoundItemInformation(OrderInfo.OrderDetailedListBean orderInfo) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        String title = "单品退款单";
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(title+"\n\n");
        StringBuilder sbTable = new StringBuilder();
        sbTable.append("商家名称: " + SpUtil.getShopName() + "\n");
        double realMoney = AppUtil.getRealMoney(orderInfo.getVipUserId(), orderInfo.getIsPromotion(), orderInfo.getIsFold(), orderInfo.getCountVipMoney(), orderInfo.getCountPromotionMoney(), orderInfo.getCountMoney());
        String moneyStr = "退款金额: " + realMoney + "元";
        sbTable.append(moneyStr + "\n");
        if (orderInfo.getVipUserId() > 0) {
            sbTable.append("退款方式: 会员卡\n");
        } else {
            sbTable.append("退款方式: 线下支付\n");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(new Date());
        sbTable.append("退款时间: " + time + "\n");
        sbTable.append("交易单号: " + orderInfo.getOrderId() + "\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText(sbTable.toString()+"\n\n\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);
    }

    /**
     * 打印退款单
     *
     * @param orderInfo
     */
    public void printRefoundInformation(OrderInfo orderInfo) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        String title = "退款单";
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(title+"\n\n");
        StringBuilder sbTable = new StringBuilder();
        sbTable.append("商家名称: " + SpUtil.getShopName() + "\n");
        String moneyStr = "退款金额: " + orderInfo.getRealAmt() + "元";
        sbTable.append(moneyStr + "\n");
        sbTable.append("支付方式: " + AppUtil.getPayType(orderInfo.getPayedMethod()) + "\n");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(new Date());
        sbTable.append("退款时间: " + time + "\n");
        sbTable.append("交易单号: " + orderInfo.getOrderNo() + "\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText(sbTable.toString()+"\n\n\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);
    }

    /**
     * 日结打印
     */
    public void printDailySettlement(DailySettlementBean bean) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        List<DailySettlementBean.CommodityListBean> commodityList = bean.getCommodityList();
        StringBuilder sbTable = new StringBuilder();
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        if (commodityList.size() > 0) {
            String title = "营业额";
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
            esc.addText(title+"\n\n");
            sbTable.append(mediumSpline + "\n");

            String headerRow1 = "名称";
            String headerRow2 = "数量";
            String headerRow3 = "金额";
            String str1 = headerRow1 + getBlankBySize(row1 - 1 - length(headerRow1));
            String str2 = headerRow2 + getBlankBySize(row2 + 2 - length(headerRow2));
            String str3 = headerRow3;
            String headerStr = str1 + str2 + str3;
            sbTable.append(headerStr + "\n");
            sbTable.append(mediumSpline + "\n");
            BigDecimal turnOverMoney = new BigDecimal(0);

            for (int i = 0; i < commodityList.size(); i++) {
                DailySettlementBean.CommodityListBean info = commodityList.get(i);
                sbTable = getBuilder(sbTable, info.getName(), info.getMoney(), info.getNumber());
                turnOverMoney = add(turnOverMoney, info.getMoney());
            }
            List<DailySettlementBean.ProductListBean> productList = bean.getProductList();
            for (int i = 0; i < productList.size(); i++) {
                DailySettlementBean.ProductListBean info = productList.get(i);
                sbTable = getBuilder(sbTable, info.getName(), info.getMoney(), info.getNumber());
                turnOverMoney = add(turnOverMoney, info.getMoney());
            }
            sbTable.append(mediumSpline + "\n");
            sbTable.append("营业额总计：" + turnOverMoney + "元");
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
            esc.addText(sbTable.toString()+"\n\n\n");
        }

        List<DailySettlementBean.RechargeListBean> rechargeList = bean.getRechargeList();
        if (rechargeList.size() > 0) {
            String title = "充值";
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
            esc.addText(title+"\n\n");
            sbTable.delete(0, sbTable.length());
            BigDecimal rechargeMoney = new BigDecimal(0);

            for (int i = 0; i < rechargeList.size(); i++) {
                DailySettlementBean.RechargeListBean rechargeBean = rechargeList.get(i);
                String rowFirst = rechargeBean.getName() + getBlankBySize(14 - length(rechargeBean.getName()));
                String rowSecond = rechargeBean.getMoney() + getBlankBySize(row2 - length(rechargeBean.getMoney() + ""));
                sbTable.append(rowFirst + rowSecond + "元\n");
                rechargeMoney = add(rechargeMoney, rechargeBean.getMoney());
            }
            sbTable.append(mediumSpline + "\n");
            sbTable.append("充值总计：" + rechargeMoney + "元\n\n\n");
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
            esc.addText(sbTable.toString()+"\n\n\n");
        }
        List<DailySettlementBean.OrderListBean> runList = bean.getOrderList();
        if (runList.size() > 0) {
            String title = "流水";
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
            esc.addText(title+"\n\n");

            sbTable.delete(0, sbTable.length());
            BigDecimal orderMoney = new BigDecimal(0);
            for (int i = 0; i < runList.size(); i++) {
                DailySettlementBean.OrderListBean orderBean = runList.get(i);
                String rowFirst = orderBean.getName() + getBlankBySize(14 - length(orderBean.getName()));
                String rowSecond = orderBean.getMoney() + getBlankBySize(row2 - length(orderBean.getMoney() + ""));
                sbTable.append(rowFirst + rowSecond + "元\n");
                orderMoney = add(orderMoney, orderBean.getMoney());
            }
            sbTable.append(mediumSpline + "\n");
            sbTable.append("流水总计：" + orderMoney + "元");
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
            esc.addText(sbTable.toString()+"\n\n\n");
        }
        esc.addText("\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);

    }

    /**
     * 单行打印文字
     *
     * @param content     打印的内容
     * @param fontSize    字体大小
     * @param isUnderline 是否加下划线
     */
    public void printAgreement(String content, int fontSize, boolean isUnderline, boolean isBoldOn) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addText(content+"\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);

    }

    /**
     * 打印标题title
     *
     * @param title
     */
    public void printTitle(String title) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER); // 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(title+"\n\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);


    }

    /**
     * 打印均分字符串
     *
     * @param part1 字符串一
     * @param part2 字符串二
     */
    public void printPartTwo(String part1, String part2, int fontSize, boolean isUnderline) {
        if (deviceManager==null)return;
        if (deviceManager.getCurrentPrinterCommand()== PrinterCommand.TSC){
            Toast.makeText(context,"请选择正确的打印机指令",Toast.LENGTH_SHORT).show();
            return;
        }
        String str = part1 + getBlankBySize(10 - length(part1)) + part2;
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addText(str+"\n");
        Vector<Byte> datas = esc.getCommand();
        deviceManager.sendDataImmediately(datas);

    }


    private StringBuilder getBuilder(StringBuilder builder, String name, double money, int count) {
        double nameSize = length(name);
        if (nameSize > row1) {
            // 列内容长度大于最大列长度,当成一行内容（换行）
            builder.append(name + "\n");
            // 数量和价格不会超过最大列宽，就不判断内容是否超出了
            String newLineSecond = money + "元" + getBlankBySize(row2 - length(count + ""));
            String newLineEnd = money + "\n";
            String newLineAll = newLineSecond + newLineEnd;
            // 左边补足row1长度空格
            builder.append(getBlankBySize(row1) + newLineAll);
        } else {
            // 正常
            String rowFirst = name + getBlankBySize(row1 - length(name));
            String rowSecond = count + getBlankBySize(row2 - length(count + ""));
            // 最后直接换行就可以了
            String rowEnd = money + "元\n";
            builder.append(rowFirst + rowSecond + rowEnd);
        }
        return builder;
    }

    private String getBlankBySize(int size) {
        String resultStr = "";
        for (int i = 0; i < size; i++) {
            resultStr += " ";
        }
        return resultStr;
    }

    private int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    private boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public BigDecimal add(BigDecimal b1, double v2) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public final static int WIDTH_PIXEL = 384;
    public final static int IMAGE_SIZE = 320;
    public void printBitmap(Bitmap bmp) {
        if (mOutputStream==null)return;
        //bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        printRawBytes(bmpByteArray);
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE;
        int newHeight = IMAGE_SIZE;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    private byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 10000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;

        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    private byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    public void printRawBytes(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printTestText(){
        printUtil.printCustomText("10号字体\n\n",10);
        printUtil.printCustomText("11号字体\n\n",11);
        printUtil.printCustomText("12号字体\n\n",12);
        printUtil.printCustomText("13号字体\n\n",13);
        printUtil.printCustomText("14号字体\n\n",14);
        printUtil.printCustomText("15号字体\n\n",15);
        printUtil.printCustomText("16号字体\n\n",16);
        printUtil.printCustomText("17号字体\n\n",17);
        printUtil.printCustomText("18号字体\n\n",18);
        printUtil.printCustomText("19号字体\n\n",19);
        printUtil.printCustomText("20号字体\n\n",20);
        printUtil.printCustomText("21号字体\n\n",21);
        printUtil.printCustomText("22号字体\n\n",22);
        printUtil.printCustomText("23号字体\n\n",23);
        printUtil.printCustomText("24号字体\n\n",24);
        printUtil.printCustomText("25号字体\n\n",25);
        printUtil.printCustomText("26号字体\n\n",26);
        printUtil.printCustomText("27号字体\n\n",27);
        printUtil.printCustomText("28号字体\n\n",28);
        printUtil.printCustomText("29号字体\n\n",29);
        printUtil.printCustomText("30号字体\n\n",30);
        printUtil.printCustomText("31号字体\n\n",31);
        printUtil.printCustomText("32号字体\n\n",32);
        printUtil.printCustomText("33号字体\n\n",33);
        printUtil.printCustomText("34号字体\n\n",34);
        printUtil.printCustomText("35号字体\n\n",35);
        printUtil.printCustomText("36号字体\n\n",36);
        printUtil.printCustomText("37号字体\n\n",37);
        printUtil.printCustomText("38号字体\n\n",38);
        printUtil.printCustomText("39号字体\n\n",39);
        printUtil.printCustomText("40号字体\n\n",40);
        printUtil.printCustomText("41号字体\n\n",41);
        printUtil.printCustomText("42号字体\n\n",42);
        printUtil.printCustomText("43号字体\n\n",43);
        printUtil.printCustomText("44号字体\n\n",44);
        printUtil.printCustomText("45号字体\n\n",45);
        printUtil.printCustomText("46号字体\n\n",46);
        printUtil.printCustomText("47号字体\n\n",47);
        printUtil.printCustomText("48号字体\n\n",48);
        printUtil.printCustomText("49号字体\n\n",49);
        printUtil.printCustomText("50号字体\n\n",50);
        printUtil.printCustomText("51号字体\n\n",51);
        printUtil.printCustomText("52号字体\n\n",52);
        printUtil.printCustomText("53号字体\n\n",53);
        printUtil.printCustomText("54号字体\n\n",54);
        printUtil.printCustomText("55号字体\n\n",55);
        printUtil.printCustomText("56号字体\n\n",56);
        printUtil.printCustomText("57号字体\n\n",57);
        printUtil.printCustomText("58号字体\n\n",58);
        printUtil.printCustomText("59号字体\n\n",59);
        printUtil.printCustomText("60号字体\n\n",60);
        printUtil.printCustomText("61号字体\n\n",61);
        printUtil.printCustomText("62号字体\n\n",62);
        printUtil.printCustomText("63号字体\n\n",63);
        printUtil.printCustomText("64号字体\n\n",64);
        printUtil.printCustomText("65号字体\n\n",65);
        printUtil.printCustomText("66号字体\n\n",66);
        printUtil.printCustomText("67号字体\n\n",67);
        printUtil.printCustomText("68号字体\n\n",68);
        printUtil.printCustomText("69号字体\n\n",69);
        printUtil.printCustomText("70号字体\n\n",70);
        printUtil.printCustomText("71号字体\n\n",71);
        printUtil.printCustomText("72号字体\n\n",72);
        printUtil.printCustomText("73号字体\n\n",73);
        printUtil.printCustomText("74号字体\n\n",74);
        printUtil.printCustomText("75号字体\n\n",75);
        printUtil.printCustomText("76号字体\n\n",76);
        printUtil.printCustomText("77号字体\n\n",77);
        printUtil.printCustomText("78号字体\n\n",78);
        printUtil.printCustomText("79号字体\n\n",79);
        printUtil.printCustomText("80号字体\n\n",80);
        printUtil.printCustomText("81号字体\n\n",81);
        printUtil.printCustomText("82号字体\n\n",82);
        printUtil.printCustomText("83号字体\n\n",83);
        printUtil.printCustomText("84号字体\n\n",84);
        printUtil.printCustomText("85号字体\n\n",85);
        printUtil.printCustomText("86号字体\n\n",86);
        printUtil.printCustomText("90号字体\n\n",90);
        printUtil.printCustomText("95号字体\n\n",95);
        printUtil.printCustomText("100号字体\n\n",100);
    }
public void onDestroy(){
    context.unregisterReceiver(receiver);
    DeviceConnFactoryManager.closeAllPort();
//    if (threadPool != null) {
//        threadPool.stopThreadPool();
//    }
}

}
