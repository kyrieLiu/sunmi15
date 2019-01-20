package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.SandQRCodeActivity;
import ys.app.pad.activity.ShoppingPayResultActivity;
import ys.app.pad.activity.vip.PayResultActivity;
import ys.app.pad.databinding.SandQRCodeBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.OrderCancelResult;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.model.OrderPayResult;
import ys.app.pad.model.OrderQueryResult;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.CashierSign;
import ys.app.pad.utils.FileHelper;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;

/**
 * Created by WBJ on 2018/3/12 16:22.
 */

public class SandQRCodeModel extends BaseActivityViewModel {
    private SandQRCodeActivity mActivity;
    private SandQRCodeBinding mBinding;
    private CustomDialog mDialog;

    private OrderPayParam orderPayParam;

    public ObservableField<Boolean> clickEnable = new ObservableField<>(false);

    private ApiClient<BaseResult> mClient;
    private RxManager mRxManager;
    private OrderQueryResult payResult;
    private String payOrderNo;
    private ApiClient<OrderPayResult> codeClient;
    private ApiClient<OrderQueryResult> queryClient;
    private String intentFrom;
    private Map<String, String> params = new HashMap<>();
    private FileHelper sdFileHelper;
    private ChargeInfo mChargeInfo;

    public SandQRCodeModel(SandQRCodeActivity mActivity, SandQRCodeBinding mBinding, ChargeInfo chargeInfo) {
        this.mActivity = mActivity;
        this.mBinding = mBinding;
        mClient = new ApiClient<>();
        codeClient = new ApiClient();
        queryClient = new ApiClient<>();
        mChargeInfo=chargeInfo;
        init();
    }

    private void init() {
        Intent intent = mActivity.getIntent();
        orderPayParam = (OrderPayParam) intent.getSerializableExtra(Constants.intent_info);
        intentFrom=intent.getStringExtra(Constants.intent_name);
        if (orderPayParam!=null) {
            mBinding.tvPersonalPay.setText("应支付金额"+orderPayParam.getAmount()+"元");
            sdFileHelper= FileHelper.getInstance().setContext(mActivity);
            loadHttpData();
        }

    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataHttp();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
        }
    }

    public void onClickNetWorkError(View view) {
        getDataHttp();
    }

    private void getDataHttp() {

        long time = System.currentTimeMillis();
        params.clear();
        payOrderNo = AppUtil.getOrderNoAppendRandom(orderPayParam.getOrderNo());
        params.put("orderNo", payOrderNo);
        params.put("amount", orderPayParam.getAmount());
        params.put("goodsName", orderPayParam.getGoodsName());
        params.put("payChannelTypeNo", orderPayParam.getPayChannelTypeNo());
        params.put("timeStamp", time + "");
        params.put("mchNo", SpUtil.getSandMchNo());
        String sign = CashierSign.getSign(SpUtil.getSandKey(), params);
        params.put("sign", sign);

        mActivity.showRDialog();
        codeClient.reqOtherURL("doPay", params, ApiRequest.RespDataType.RESPONSE_JSON, Constants.base_sand_url)
                .updateUI(new Callback<OrderPayResult>() {
                    @Override
                    public void onSuccess(OrderPayResult orderPayResult) {
                        mActivity.hideRDialog();
                        if (null != orderPayResult.getData()) {
                            showPayImage(orderPayResult.getData().getQrCodeImg());
                        } else {
                            showToast(mActivity, orderPayResult.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isNetWorkErrorVisible.set(true);
                        mActivity.hideRDialog();
                    }
                });
    }


    private void showPayImage(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            showLongToast(mActivity, "未查询到收款二维码");
        } else {
            switch (orderPayParam.getPayChannelTypeNo()){
                case "0201":
                case "0205":
                    sdFileHelper.savePicture("sandWX",imageUrl,"微信支付",orderPayParam.getAmount()+"");
                    break;
                case "0104":
                case "0106":
                    sdFileHelper.savePicture("sandZFB",imageUrl,"支付宝支付",orderPayParam.getAmount()+"");
                    break;
            }
            Glide.with(mActivity).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Toast.makeText(mActivity, "未查询到收款二维码", Toast.LENGTH_LONG).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).placeholder(R.mipmap.load_img_normal).dontAnimate().into(mBinding.ivQRCodeWeixin);
            clickEnable.set(true);
        }
    }

    public void confirmPay(View view) {
        if (mDialog == null) {
            mDialog = new CustomDialog(mActivity);
            mDialog.setCancelVisiable();
        }
        mDialog.setContent("为确保资金安全，请核实支付信息,确认收款吗?");
        mDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
                    mActivity.showRDialog();
                    querySandOrder();
                    if (Constants.T1mini.equals(Build.MODEL)){
                        AidlUtil.getInstance().setTextToT1mini(null);
                    }
                } else {//无网显示断网连接
                    showToast(mActivity, Constants.network_error_warn);
                }

            }
        });
        mDialog.show();
    }


    private int queryCount = 0;

    //查询支付结果
    private void querySandOrder() {
        clickEnable.set(false);
        Map map = getSandPayMap();
        queryClient.reqOtherURL("querySandOrder", map, ApiRequest.RespDataType.RESPONSE_JSON, Constants.base_sand_url)
                .updateUI(new Callback<OrderQueryResult>() {
                    @Override
                    public void onSuccess(OrderQueryResult orderQueryResult) {

                        if ("SUCCESS".equals(orderQueryResult.getPayStatus())) {
                            SandQRCodeModel.this.payResult = orderQueryResult;
                            updatePayResult();
                            //reFund(orderQueryResult);
                        } else {
                            mActivity.hideRDialog();
                            showLongToast(mActivity, orderQueryResult.getMsg());
                            clickEnable.set(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //如果网络异常继续请求数据
                        queryCount++;
                        if (queryCount >= 4) {
                            mActivity.hideRDialog();
                            clickEnable.set(true);
                            Toast.makeText(mActivity, "网络异常,查询失败,请稍后再试", Toast.LENGTH_LONG).show();
                        } else {
                            querySandOrder();
                        }
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

    //构造杉德支付参数
    private Map getSandPayMap() {
        params.clear();
        long time = System.currentTimeMillis();
        params.put("mchNo", SpUtil.getSandMchNo());
        params.put("orderNo", payOrderNo);
        params.put("timeStamp", time + "");
        String sign = CashierSign.getSign(SpUtil.getSandKey(), params);
        params.put("sign", sign);
        return params;
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
        params.put("pay_info", payResult.getMsg());
        params.put("out_trade_no", payResult.getOrderNo());
        params.put("cashier_trade_no", payResult.getGwTradeNo());
        params.put("mcode", SpUtil.getBranchId() + "");
        params.put("device_en", SpUtil.getShopId());
        params.put("trade_status", payResult.getPayStatus());
        params.put("time_create", payResult.getCreateDate());
        params.put("time_end", payResult.getTimeStamp());
        String money=payResult.getPayAmount().replaceAll(",","");
        double payAmount = Double.parseDouble(money);
        String totalFee = Math.round(payAmount * 100) + "";
        params.put("total_fee", totalFee);
        params.put("pay_fee", totalFee);
        switch (payResult.getPayChannelTypeNo()) {//跟旺pos的回调保持一致
            case "0205":
                params.put("pay_type", "0012");
                break;
            case "0106":
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


    /**
     * 若不支付则取消订单
     */
    public void cancelOrder() {
        long time = System.currentTimeMillis();
        Map<String, String> params = new HashMap();
        params.put("mchNo", SpUtil.getSandMchNo());
        params.put("orderNo", payOrderNo);
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
                            mActivity.setResult(Constants.result_code);
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

    public void onBackPress() {

        final CustomDialog  cancelDialog = new CustomDialog(mActivity);
        cancelDialog.setCancelVisiable();
        cancelDialog.setContent("订单已生成,确认取消支付吗?");
        cancelDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog.dismiss();
                if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
                    if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
                        cancelOrder();
                    } else {//无网显示断网连接
                        showToast(mActivity, Constants.network_error_warn);
                    }
                } else {//无网显示断网连接
                    showToast(mActivity, Constants.network_error_warn);
                }

            }
        });
        cancelDialog.show();
    }
    public void onPause(){
        sdFileHelper.onPause();
    }

    public void onDestroy() {
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
    }
}
