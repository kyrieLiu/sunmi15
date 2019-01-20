package ys.app.pad.shangmi.screen.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.SF;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.callback.QueryCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import sunmi.ds.data.DataPacket;
import ys.app.pad.R;
import ys.app.pad.activity.vice.CommitOrderViceActivity;
import ys.app.pad.activity.vice.ViceWelcomeActivity;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.shangmi.screen.ScreenModel;
import ys.app.pad.utils.FileHelper;
import ys.app.pad.utils.SpUtil;

import static ys.app.pad.shangmi.screen.utils.DataModel.COMMIT_ORDER_MENU;

/**
 * Created by liuyin on 2018/3/8.
 * 大屏与小屏交互的工具
 */

public class ViceConnectUtils {
    private Activity activity;
    private static final String TAG = "ViceConnectUtils";
    //双屏通讯帮助类
    private DSKernel mDSKernel = null;

    private Handler myHandler;
    private Gson gson = new Gson();
    private Intent intent = new Intent();

    private static ViceConnectUtils utils;

    public static ViceConnectUtils getInstance() {
        if (utils == null) {
            utils = new ViceConnectUtils();
        }
        return utils;
    }

    public ViceConnectUtils setContext(Activity activity) {
        this.activity = activity;

        if (mDSKernel != null) {
            //mDSKernel.checkConnection();
        } else {
            myHandler = new MyHandler(activity);
            initSdk();
        }
        return utils;
    }


    private ViceConnectUtils() {
    }

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj =  activity.getString(R.string.unconnect_main_service);
            myHandler.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = activity.getString(R.string.connect_main_service);
                    break;
                case VICE_SERVICE_CONN:
                    message.obj =  activity.getString(R.string.connect_vice_service);
                    break;
                case VICE_APP_CONN:
                    message.obj =  activity.getString(R.string.connect_vice_dsd);
                    break;
                default:
                    break;
            }
            //myHandler.sendMessage(message);
        }
    };

    /**
     * 双屏通讯消息回调
     */
    private IReceiveCallback mIReceiveCallback = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {
            Log.d(TAG, "onReceiveData: ---------->" + data.data);
        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {
            Log.d(TAG, "onReceiveCMD: ------------------->" + cmd.data);
            ScreenModel data = gson.fromJson(cmd.data, ScreenModel.class);
            Log.d(TAG, "onReceiveCMD: ------------------->" + data.dataModel+"  COMMIT_ORDER_MENU"+COMMIT_ORDER_MENU);
            switch (data.dataModel) {
                //副屏显示单张图片
                case COMMIT_ORDER_MENU:
                    intent.setClass( activity, CommitOrderViceActivity.class);
                    intent.putExtra("orderData", (String) data.data);
                    activity.startActivity(intent);
                    break;
                case SAVE_SHOP_INFORMATION:
                    String shopData= (String) data.data;
                    try {
                        JSONObject object=new JSONObject(shopData);
                        String shopName=object.getString("shopName");
                        SpUtil.setShopName(shopName);
                        intent.setClass(activity, ViceWelcomeActivity.class);
                        activity.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOW_WELCOME_14:
                    intent.setClass(activity, ViceWelcomeActivity.class);
                    activity.startActivity(intent);
                    break;

                case CLEAN_FILES:
                    Log.d(TAG, "delete file is ----->" + data.data);
                    FileHelper.deleteDir((String)data.data);
                    break;
                case GETVICECACHEFILESIZE://获取副屏缓存文件大小
                    Log.d(TAG, "获取副屏缓存文件大小----->" + data.data);
                    long size = 0L;
                    File file = new File((String)data.data);
                    if (file.exists()) {
                        size = getFilesSize(file);
                    }
                    mDSKernel.sendResult(cmd.sender, String.valueOf(size), cmd.taskId, null);
                    break;
                default:
                    break;
            }
        }
    };

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(activity, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
    }

    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://消息提示用途
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    private long getFilesSize(File file) {
        long size = 0L;
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.d(TAG, "filename----->" + files[i]);
            if (files[i].isDirectory()) {
                size = size + getFilesSize(files[i]);
            } else {
                size = size + getFileSize(files[i]);
            }
        }
        return size;
    }

    /**
     * @param file
     */
    private long getFileSize(File file) {
        long size = 0L;
        if (file.exists()) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                size = fileInputStream.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, file.getName() + "大小----》" + size);
        return size;
    }






    //显示文本信息
    public void sendTextMessage(String title, String content) {
        if (SpUtil.getModelVice().equals(""))return;
        JSONObject textJson = new JSONObject();
        try {
            textJson.put("title", title);//title为上面一行的标题内容
            textJson.put("content", content);//content为下面一行的内容
            String jsonStr = textJson.toString();
            DataPacket packet = UPacketFactory.buildShowText(DSKernel.getDSDPackageName(), jsonStr, new ViceCallback());//第一个参数是接收数据的副显应用的包名，这里参照Demo就可以,第二个参数是要显示的内容字符串，第三个参数为结果回调。

            mDSKernel.sendData(packet);//调用sendData方法发送文本


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //显示待支付信息
    public void sendPayMessage(String payMethod, String money, String path) {
        if (SpUtil.getModelVice().equals(""))return;
        JSONObject json = new JSONObject();
        try {
            json.put("title", payMethod);
            json.put("content", money);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDSKernel.sendFile(DSKernel.getDSDPackageName(), json.toString(), path, new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                //显示图片
                try {
                    JSONObject json = new JSONObject();
                    json.put("dataModel", "QRCODE");
                    json.put("data", "default");
                    mDSKernel.sendCMD(SF.DSD_PACKNAME, json.toString(), l, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSendFail(int i, String s) {
                Message message = new Message();
                message.what = 1;
                message.obj = "副屏信息发送失败";
                myHandler.sendMessage(message);
            }

            @Override
            public void onSendProcess(long l, long l1) {

            }
        });
    }


    //展示订单信息
    public void showMenuMessage(List<SummitOrderInfo> orderInfoList){
        if (!SpUtil.getModelVice().equals("t1sub14")){
            return;
        }
        JSONObject json = new JSONObject();
        Gson gson=new Gson();
        String orderJson=gson.toJson(orderInfoList);
        try {
            json.put("dataModel", "COMMIT_ORDER_MENU");
            json.put("data", orderJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket packet= new DataPacket.Builder(DSData.DataType.CMD).recPackName(activity.getPackageName()).data(json.toString())
                .addCallback(new ViceCallback()).isReport(true).build();
        mDSKernel.sendData(packet);
    }
    //保存店铺信息
    public void saveShopInformation(){
//        if (!SpUtil.getModelVice().equals("t1sub14")){
//            return;
//        }
        JSONObject json = new JSONObject();
        try {
            json.put("shopName", SpUtil.getShopName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("dataModel", "SAVE_SHOP_INFORMATION");
            json.put("data", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket packet= new DataPacket.Builder(DSData.DataType.CMD).recPackName(activity.getPackageName()).data(json.toString())
                .addCallback(new ViceCallback()).isReport(true).build();
        mDSKernel.sendData(packet);
    }
    public void sendMessage(){
        String json = UPacketFactory.createJson(DataModel.IMAGES, "");
        mDSKernel.sendCMD(activity.getPackageName(), json, 1, null);
    }
    //展示欢迎页面
    public void showWelcomeMessage() {
        if (SpUtil.getModelVice().equals(""))return;
        try {
            JSONObject json = new JSONObject();
            json.put("data", "gaolulin");
            if (SpUtil.getModelVice().equals("t1sub14")){
                json.put("dataModel", "SHOW_WELCOME_14");
                DataPacket packet= new DataPacket.Builder(DSData.DataType.CMD).recPackName(activity.getPackageName()).data(json.toString())
                        .addCallback(new ViceCallback()).isReport(true).build();
                mDSKernel.sendData(packet);
            }else{
                json.put("dataModel", "SHOW_IMG_WELCOME");
                if (mDSKernel!=null) mDSKernel.sendCMD(SF.DSD_PACKNAME, json.toString(), -1, null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getViceModel(){
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("dataModel", "GET_MODEL");
            jsonObject2.put("data", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket p2 = new DataPacket.Builder(DSData.DataType.CMD).recPackName(SF.SUNMI_DSD_PACKNAME).data(jsonObject2.toString())
                .addCallback(new ViceCallback()).build();
        mDSKernel.sendQuery(p2, new QueryCallback() {
            @Override
            public void onReceiveData(final DSData data) {
                Log.d("highsixty", "onReceiveData: ------------>" + data.data);
                SpUtil.setModelVice(data.data);

            }
        });
    }

    private class ViceCallback implements ISendCallback{

        @Override
        public void onSendSuccess(long taskId) {
            Log.d(TAG,"发送成功");
            Message message = myHandler.obtainMessage();
            message.what = 1;
            message.obj = "副屏信息发送成功";
            myHandler.sendMessage(message);
        }

        @Override
        public void onSendFail(int errorId, String errorInfo) {
            Log.d(TAG,"发送失败");
            Message message = myHandler.obtainMessage();
            message.what = 1;
            message.obj = "副屏信息发送失败";
            myHandler.sendMessage(message);
        }

        @Override
        public void onSendProcess(long totle, long sended) {
        }
    }
    public void showToast(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onDestroy() { //如果存在activity跳转，需要做清理操作
        if (mDSKernel != null) {
            mDSKernel.onDestroy();
            mDSKernel = null;
        }
    }

}
