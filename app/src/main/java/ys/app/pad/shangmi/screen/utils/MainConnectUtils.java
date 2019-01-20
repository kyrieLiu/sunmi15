package ys.app.pad.shangmi.screen.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.utils.SpUtil;

/**
 * Created by liuyin on 2018/3/8.
 * 大屏与小屏交互的工具
 */

public class MainConnectUtils {
    private Activity activity;
    private DSKernel mDSKernel = null;
    private MyHandler myHandler;
    private static final String TAG = "MainConnectUtils";

    private static MainConnectUtils utils;

    public static MainConnectUtils getInstance() {
        if (utils == null) {
            utils = new MainConnectUtils();
        }
        return utils;
    }

    public MainConnectUtils setContext(Activity activity) {
        this.activity = activity;
        Log.d("info","mDSKernel  =="+mDSKernel);
        if (mDSKernel != null) {
            //mDSKernel.checkConnection();
        } else {
            myHandler = new MyHandler(activity);
            initSdk();
        }
        return utils;
    }


    private MainConnectUtils() {
    }


    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj = "与远程服务连接中断";
            myHandler.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    //message.obj = "与远程服务绑定成功";
                    break;
                case VICE_SERVICE_CONN:
                    break;
                case VICE_APP_CONN:
                    message.obj = "与副屏通讯正常";
                    myHandler.sendMessage(message);
                    DataPacket dsPacket = UPacketFactory.buildOpenApp(activity.getPackageName(), null);
                    mDSKernel.sendCMD(dsPacket);
                    getViceModel();
                    break;
                default:
                    break;
            }

        }
    };

    private IReceiveCallback mIReceiveCallback = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {
        }

        @Override
        public void onReceiveFile(DSFile file) {
        }

        @Override
        public void onReceiveFiles(DSFiles files) {
        }

        @Override
        public void onReceiveCMD(DSData cmd) {
            //showToast("接收到副屏发来的消息"+cmd.data);
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
        if (!SpUtil.getModelVice().equals("t1sub14")){
            return;
        }
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
            //myHandler.sendMessage(message);
        }

        @Override
        public void onSendFail(int errorId, String errorInfo) {
            Log.d(TAG,"发送失败");
            Message message = myHandler.obtainMessage();
            message.what = 1;
            message.obj = "副屏信息发送失败";
            myHandler.sendMessage(message);
            initSdk();
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

    public void onPause() { //如果存在activity跳转，需要做清理操作
    }
    public void onDestroy(){
        if (mDSKernel != null) {
            mDSKernel.onDestroy();
            mDSKernel = null;
        }
    }

}
