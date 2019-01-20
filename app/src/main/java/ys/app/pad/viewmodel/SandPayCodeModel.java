package ys.app.pad.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.t1miniscan.ScanActivity;
import ys.app.pad.shangmi.t1miniscan.ScanConfig;
import ys.app.pad.shangmi.t1miniscan.utils.LogUtil;
import ys.app.pad.shangmi.t1miniscan.utils.SoundUtils;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.ScannerGunPayCodeActivity;
import ys.app.pad.activity.ShoppingPayResultActivity;
import ys.app.pad.activity.vip.PayResultActivity;
import ys.app.pad.databinding.SandPayCodeBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.OrderCancelResult;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.model.OrderPayResult;
import ys.app.pad.shangmi.t1miniscan.ScanConfig;
import ys.app.pad.shangmi.t1miniscan.utils.LogUtil;
import ys.app.pad.shangmi.t1miniscan.utils.SoundUtils;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.CashierSign;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by WBJ on 2018/3/13 11:37.
 */

public class SandPayCodeModel extends BaseActivityViewModel implements SurfaceHolder.Callback {
    public static final String TAG = SandPayCodeModel.class.getSimpleName();
    ScannerGunPayCodeActivity mActivity;
    SandPayCodeBinding mBinding;

    private OrderPayParam orderPayParam;

    public ObservableField<String> obPayStatus = new ObservableField<>();
    public ObservableField<Boolean> isT1miniShow = new ObservableField<>(false);
    private RxManager mRxManager;
    private String sandPayNO;
    private Map<String, String> params = new HashMap<>();
    private ApiClient<BaseResult> mClient;
    private OrderPayResult payResult;
    private String intentFrom;
    private ChargeInfo mChargeInfo;

    //T1mini特殊机型
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private ImageScanner mImageScanner;//声明扫描器
    private Handler mAutoFocusHandler;
    private AsyncDecode asyncDecode;
    private SoundUtils soundUtils;
    private AtomicBoolean isRUN = new AtomicBoolean(false);

    public SandPayCodeModel(ScannerGunPayCodeActivity mActivity, SandPayCodeBinding mBinding, ChargeInfo chargeInfo) {
        this.mActivity = mActivity;
        this.mBinding = mBinding;
        mClient = new ApiClient<>();
        mChargeInfo=chargeInfo;
        init();
    }

    private void init() {
        Intent intent = mActivity.getIntent();
        intentFrom=intent.getStringExtra(Constants.intent_name);
        orderPayParam = (OrderPayParam) intent.getSerializableExtra(Constants.intent_info);
        sandPayNO= AppUtil.getOrderNoAppendRandom(orderPayParam.getOrderNo());
        mBinding.tvPersonalPay.setText("应支付金额"+orderPayParam.getAmount()+"元");

        if ("T1mini".equals(Build.MODEL)){
            isT1miniShow.set(true);
            setConfig();
            initT1Mini();
        }
    }

    public void pay(String authCode){
        long time = System.currentTimeMillis();
        Map<String, String> params = new HashMap();

        params.put("mchNo", SpUtil.getSandMchNo());
        params.put("orderNo", sandPayNO);
        params.put("amount", orderPayParam.getAmount());
        params.put("goodsName", orderPayParam.getGoodsName());
        params.put("payChannelTypeNo", orderPayParam.getPayChannelTypeNo());
        params.put("timeStamp", time + "");
        params.put("authCode", authCode);
        String sign = CashierSign.getSign(SpUtil.getSandKey(), params);
        params.put("sign", sign);

        ApiClient<OrderPayResult> client=new ApiClient();
        mActivity.showRDialog();
        client.reqOtherURL("doPay",params, ApiRequest.RespDataType.RESPONSE_JSON,Constants.base_sand_url)
                .updateUI(new Callback<OrderPayResult>() {
                    @Override
                    public void onSuccess(OrderPayResult orderPayResult) {
                        mActivity.hideRDialog();
                        if ("SUCCESS".equals(orderPayResult.getResult())){
                            SandPayCodeModel.this.payResult = orderPayResult;
                            updatePayResult();
                            if (Constants.T1mini.equals(Build.MODEL)) {
                                AidlUtil.getInstance().setTextToT1mini(null);
                            }
                        }else{
                            showToast(mActivity,orderPayResult.getMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        obPayStatus.set("网络异常,请稍后再试");
                    }
                });

    }

    private int count = 0;

    private void updatePayResult() {
        Map map = getRequestParams();
        mClient.reqLongTimeApi("shandeVirtualReturnUrl", map, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        operateNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        count++;
                        if (count >= 2) {
                            Toast.makeText(mActivity, e.getMessage() + "", Toast.LENGTH_LONG).show();
                            updateOtherURL();//如果两次没有执行成功执行另外的接口
                        } else {
                            updatePayResult();
                        }
                    }
                });
    }

    private void updateOtherURL() {
        final Map map = getRequestParams();

        mClient.reqOtherURL("shandeVirtualReturnUrl", map, ApiRequest.RespDataType.RESPONSE_JSON, Constants.base_update_url)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        operateNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        count++;
                        if (count >= 4) {
                            mActivity.hideRDialog();
                            Toast.makeText(mActivity, e.getMessage() + "", Toast.LENGTH_LONG).show();
                            operateNext();
                            updateWithTimer(map);//如果更新失败,进行循环调用
                        } else {
                            updateOtherURL();
                        }
                    }
                });
    }

    private Timer timer;

    //如果更新失败,进行循环调用
    private void updateWithTimer(final Map map) {
        final ApiClient<BaseResult> updateClient = new ApiClient<>();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateClient.reqOtherURL("shandeVirtualReturnUrl", map, ApiRequest.RespDataType.RESPONSE_JSON, Constants.base_update_url)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult result) {
                                Log.i("okHttp", "okHttp更新完成");
                                timer.cancel();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Log.i("okHttp", "okHttp重新更新");
                                updateClient.reset();
                            }
                        });
            }
        };
        timer.schedule(task, 0, 10000);
    }


    /*
       0103：支付宝条码支付
       0104：支付宝扫码支付
       0105: 平安银行支付宝条码
       0106: 平安银行支付宝扫码
       0201：微信扫码支付
       0202：微信H5支付(暂未开放)
       0203：微信刷卡支付
       0204: 平安银行微信刷卡
       0205: 平安银行微信扫码
       0301: 银联条码支付
       0302：银联扫码支付
        */
    private Map getRequestParams() {
        params.clear();
        params.put("pay_info", "业务请求成功");
        params.put("out_trade_no", payResult.getData().getOrderNo());
        params.put("cashier_trade_no", payResult.getData().getGwTradeNo());
        params.put("mcode", SpUtil.getBranchId() + "");
        params.put("device_en", SpUtil.getShopId());
        params.put("trade_status", "SUCCESS");
        params.put("time_create", payResult.getData().getCompleteDate());
        params.put("time_end", payResult.getData().getCompleteDate());
        String money=payResult.getData().getAmount().replaceAll(",","");
        double payAmount = Double.parseDouble(money);
        String totalFee = Math.round(payAmount * 100) + "";
        params.put("total_fee", totalFee);
        params.put("pay_fee", totalFee);
        switch (orderPayParam.getPayChannelTypeNo()) {//跟旺pos的回调保持一致
            case "0204":
                params.put("pay_type", "0012");
                break;
            case "0105":
                params.put("pay_type", "0013");
                break;
        }

        return params;
    }


    private void operateNext(){
        if ("shoppingViewModel".equals(intentFrom)){
            toNextPage( false);
        }else{
            vipChargeNext();
        }

    }
    private void toNextPage( boolean isVipPay) {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        mRxManager.post(Constants.bus_type_http_result, Constants.type_order_pay_finish);
        Intent intent = new Intent(mActivity, ShoppingPayResultActivity.class);
        intent.putExtra(Constants.intent_orderNo, orderPayParam.getOrderNo());
        intent.putExtra(Constants.intent_is_vip_pay, isVipPay);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void vipChargeNext(){
        mActivity.hideRDialog();
        if (mRxManager == null) mRxManager = new RxManager();
        mRxManager.post(Constants.bus_type_http_result, Constants.type_charge_finish);
        Intent intent = new Intent(mActivity, PayResultActivity.class);
        intent.putExtra(Constants.intent_info, mChargeInfo);
        intent.putExtra(Constants.intent_orderNo, orderPayParam.getOrderNo());
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    public void onBackPress() {

        final CustomDialog cancelDialog = new CustomDialog(mActivity);
        cancelDialog.setCancelVisiable();
        cancelDialog.setContent("订单已生成,确认取消支付吗?");
        cancelDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog.dismiss();
                    if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
                        cancelOrder();
                    } else {//无网显示断网连接
                        showToast(mActivity, Constants.network_error_warn);
                    }


            }
        });
        cancelDialog.show();
    }

    /**
     * 若不支付则取消订单
     */
    public void cancelOrder() {
        long time = System.currentTimeMillis();
        Map<String, String> params = new HashMap();
        params.put("mchNo", SpUtil.getSandMchNo());
        params.put("orderNo", sandPayNO);
        params.put("timeStamp", time + "");
        String sign = CashierSign.getSign(SpUtil.getSandKey(), params);
        params.put("sign", sign);

        mActivity.showRDialog();
        ApiClient<OrderCancelResult> client = new ApiClient<>();
        client.reqOtherURL("cancelPay", params, ApiRequest.RespDataType.RESPONSE_JSON, Constants.base_sand_url)
                .updateUI(new Callback<OrderCancelResult>() {
                    @Override
                    public void onSuccess(OrderCancelResult cancelResult) {
                        mActivity.hideRDialog();
                        if ("SUCCESS".equals(cancelResult.getResult())||"TRADE_NOT_EXSITS".equals(cancelResult.getResult())||"API_ERROR_3RD".equals(cancelResult.getResult())||"PARAM_ERROR".equals(cancelResult.getResult())){
                            mActivity.finish();
                        }else{
                            showToast(mActivity,"订单已支付,不能取消");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });
    }

    public void onDestroy() {
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
    }

    private void setConfig() {
        Log.i(TAG, "setConfig: ");

        LogUtil.e(TAG, "intent.getExtras() != nul");
        //当前分辨率
        ScanConfig.CURRENT_PPI = 0X0003;
        //扫描完成声音提示
        ScanConfig.PLAY_SOUND = true;
        //扫描完成震动
        ScanConfig.PLAY_VIBRATE = false;
        //识别反色二维码
        ScanConfig.IDENTIFY_INVERSE_QR_CODE = true;
        //识别画面中多个二维码
        ScanConfig.IDENTIFY_MORE_CODE = false;
        //是否显示设置按钮
//            ScanConfig.IS_SHOW_SETTING = intent.getBooleanExtra("IS_SHOW_SETTING", true);
        //是否显示选择相册按钮
//            ScanConfig.IS_SHOW_ALBUM = intent.getBooleanExtra("IS_SHOW_ALBUM", true);
        //是否显示闪光灯
        ScanConfig.IS_OPEN_LIGHT = true;
        //是否是循环模式
        ScanConfig.SCAN_MODE = false;

    }

    private void initT1Mini() {
        mSurfaceView = mBinding.surfaceView;
        mAutoFocusHandler = new Handler();
        mHolder = mSurfaceView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        mImageScanner = new ImageScanner();//创建扫描器
        mImageScanner.setConfig(0, Config.X_DENSITY, 2);//行扫描间隔
        mImageScanner.setConfig(0, Config.Y_DENSITY, 2);//列扫描间隔
    }

    private void initScanConfig() {
        //是否开启同一幅图一次解多个条码,0表示只解一个，1为多个
        if (ScanConfig.IDENTIFY_MORE_CODE) {
            mImageScanner.setConfig(0, Config.ENABLE_MULTILESYMS, 1);
        } else {
            mImageScanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0);
        }
        //是否开启识别反色二维码,0表示不识别，1为识别
        if (ScanConfig.IDENTIFY_INVERSE_QR_CODE) {
            mImageScanner.setConfig(0, Config.ENABLE_INVERSE, 1);
        } else {
            mImageScanner.setConfig(0, Config.ENABLE_INVERSE, 0);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceCreated");
//        showToast("surfaceCreated");
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            LogUtil.e(TAG, "Camera.open()");
//            showToast("Camera.open()");
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e(TAG, "surfaceChanged");
//        showToast("surfaceChanged");
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            setCameraParameters();
//            mCamera.setDisplayOrientation(90);//竖屏显示
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            LogUtil.e("DBG", "Error starting camera preview: " + e.getMessage());
//            showToast("DBG"+"Error starting camera preview: ");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceDestroyed");
//        showToast("surfaceDestroyed");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    // 获取最佳的屏幕显示尺寸
    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    private void setCameraParameters() {
        if (mCamera == null) return;
        LogUtil.e(TAG, "ScanConfig.CURRENT_PPI=" + ScanConfig.CURRENT_PPI);
        //摄像头预览分辨率设置和图像放大参数设置，非必须，根据实际解码效果可取舍
        Camera.Parameters parameters = mCamera.getParameters();

        WindowManager manager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        ScanConfig.BEST_RESOLUTION = getBestCameraResolution(parameters, new Point(display.getWidth(), display.getHeight()));

        parameters.setPreviewSize(320, 240);
        mCamera.setParameters(parameters);
    }

    /**
     * 预览数据
     */
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();//获取预览分辨率

            //创建解码图像，并转换为原始灰度数据，注意图片是被旋转了90度的
            Image source = new Image(size.width, size.height, "Y800");
            //图片旋转了90度，将扫描框的TOP作为left裁剪
            source.setData(data);//填充数据
            asyncDecode = new AsyncDecode();
            asyncDecode.execute(source);//调用异步执行解码
        }
    };


    private class AsyncDecode extends AsyncTask<Image, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Image... params) {

            final ArrayList<HashMap<String, String>> result = new ArrayList<>();
            Image src_data = params[0];//获取灰度数据
            //解码，返回值为0代表失败，>0表示成功
            final int data = mImageScanner.scanImage(src_data);
            if (data != 0) {
                playBeepSoundAndVibrate();//解码成功播放提示音
                SymbolSet syms = mImageScanner.getResults();//获取解码结果
                for (Symbol sym : syms) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put(ScanConfig.TYPE, sym.getSymbolName());
                    temp.put(ScanConfig.VALUE, sym.getResult());
                    result.add(temp);
                    break;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);

            if (!result.isEmpty()) {
                LogUtil.e(TAG, "!result.isEmpty()");
                String result_code = result.get(0).get(ScanConfig.VALUE);
                mBinding.etAuthCode.setText(result_code);
            } else {
                isRUN.set(false);
            }
        }

    }

    /**
     * 自动对焦回调
     */
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (mAutoFocusHandler != null) {
                mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        }
    };

    //自动对焦
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (null == mCamera || null == autoFocusCallback) {
                return;
            }
            mCamera.autoFocus(autoFocusCallback);
        }
    };

    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(mActivity, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }


    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
     /*private final OnCompletionListener beepListener = new OnCompletionListener()
     {
 		public void onCompletion(MediaPlayer mediaPlayer)
 		{
 			mediaPlayer.seekTo(0);
 		}
 	};*/

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null && ScanConfig.PLAY_SOUND) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (ScanConfig.PLAY_VIBRATE) {
            Vibrator vibrator = (Vibrator) mActivity.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
}
