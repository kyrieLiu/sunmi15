package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.PersonPayActivity;
import ys.app.pad.activity.ShoppingPayResultActivity;
import ys.app.pad.activity.vip.PayResultActivity;
import ys.app.pad.databinding.PersonalPayBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AllotSelectModel;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.FileHelper;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;


/**
 * Created by liuyin on 2017/11/1.
 */

public class PersonalPayModel extends BaseActivityViewModel {
    public ObservableBoolean clickEnable = new ObservableBoolean(false);
    private ApiClient<BaseListResult<AllotSelectModel>> mListClient;
    private PersonPayActivity mActivity;
    private PersonalPayBinding binding;
    private String pay_type;
    private CustomDialog mDialog;
    private  ApiClient<BaseResult> mClient;
    private String total_fee="0";
    private RxManager mRxManager;
    private String intentFrom;
    private ChargeInfo mChargeInfo;
    private FileHelper helper;
    private  double realOrderMoney;
    private Map<String, String> params = new HashMap<>();
    private String orderNO;
    public ObservableBoolean isXianJin=new ObservableBoolean();

    public PersonalPayModel(PersonPayActivity mActivity, PersonalPayBinding binding, String pay_type, String orderMoney, String intentFrom, ChargeInfo chargeInfo, String orderNO) {
        mListClient = new ApiClient<>();
        mClient=new ApiClient<>();
        this.mActivity = mActivity;
        this.binding=binding;
        this.intentFrom=intentFrom;
        mChargeInfo=chargeInfo;
        orderMoney=orderMoney.replaceAll(",","");
        realOrderMoney = Double.parseDouble(orderMoney);
        total_fee= Math.round(realOrderMoney *100) + "";
        this.orderNO=orderNO;
        this.pay_type = pay_type;
        helper= FileHelper.getInstance().setContext(mActivity);
        binding.tvPersonalPay.setText("应支付金额"+orderMoney+"元");

        if ("1001".equals(pay_type)||"1006".equals(pay_type)){
            isXianJin.set(true);
            clickEnable.set(true);
            if ("1001".equals(pay_type)){
                binding.tvPersonalPayWarn.setText("收取现金后请点击确认收款");
            }else{
                binding.tvPersonalPayWarn.setText("请确认第三方工具刷卡成功后点击确认收款");
            }

        }else{
            isXianJin.set(false);
            binding.tvPersonalPayWarn.setText("请顾客扫描二维码进行收款");
            initView();
        }

    }

    private void initView() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        mActivity.showRDialog();
        mListClient.reqApi("selectBranchShop", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AllotSelectModel>>() {
                    @Override
                    public void onSuccess(BaseListResult<AllotSelectModel> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            AllotSelectModel model = result.getData().get(0);

                            if ("0112".equals(pay_type)) {
                                showQRCode(model.getWechatImg());
                            } else {
                                showQRCode(model.getAliImg());
                            }
                        } else {
                            showToast(mActivity, result.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });

    }

    private void showQRCode(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            showLongToast(mActivity,"未查询到收款二维码,请联系店长上传二维码");
        }else{
            if ("0112".equals(pay_type)) {
                helper.savePicture("personalWX",imageUrl,"个人微信",realOrderMoney+"");

            } else {
                helper.savePicture("personalZFB",imageUrl,"个人支付宝",realOrderMoney+"");
            }

            Glide.with(mActivity).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Toast.makeText(mActivity,"未查询到收款二维码,请联系店长上传二维码",Toast.LENGTH_LONG).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).placeholder(R.mipmap.load_img_normal).dontAnimate().into(binding.ivQRCode);
            clickEnable.set(true);
        }
    }

    public void confirmPay(View view){
        if(mDialog == null){
            mDialog = new CustomDialog(mActivity);
            mDialog.setCancelVisiable();
        }
        mDialog.setContent("为确保资金安全，请核实支付信息,确认收款吗?");
        mDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                updatePayResult();
                if (Build.MODEL.equals(Constants.T1mini)){
                    AidlUtil.getInstance().setTextToT1mini(null);
                }
            }
        });
        mDialog.show();
    }
    private int count=0;
    private void updatePayResult() {
        Map map=getRequestParams();
        mActivity.showRDialog();
        clickEnable.set(false);
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
                        if (count>=2){
                            Toast.makeText(mActivity,e.getMessage()+"",Toast.LENGTH_LONG).show();
                            updateOtherURL();//如果两次没有执行成功执行另外的接口
                        }else{
                            updatePayResult();
                        }
                    }
                });
    }
    private void updateOtherURL(){
        final Map map=getRequestParams();

        mClient.reqOtherURL("shandeVirtualReturnUrl", map, ApiRequest.RespDataType.RESPONSE_JSON,Constants.base_update_url)
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
                        if (count>=4){
                            mActivity.hideRDialog();
                            Toast.makeText(mActivity,e.getMessage()+"",Toast.LENGTH_LONG).show();
                            operateNext();
                            updateWithTimer(map);//如果更新失败,进行循环调用
                        }else{
                            updateOtherURL();
                        }
                    }
                });
    }

    private Timer timer;
    //如果更新失败,进行循环调用
    private void updateWithTimer(final Map map){
        final ApiClient<BaseResult> updateClient=new ApiClient<>();
        timer = new Timer();
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                updateClient.reqOtherURL("shandeVirtualReturnUrl", map, ApiRequest.RespDataType.RESPONSE_JSON,Constants.base_update_url)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult result) {
                                Log.i("okHttp","okHttp更新完成");
                                timer.cancel();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Log.i("okHttp","okHttp重新更新");
                                updateClient.reset();
                            }
                        });
            }
        };
        timer.schedule(task,0,10000);
    }

    private Map getRequestParams(){
        params.clear();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        params.put("pay_info", "个人支付");
        params.put("out_trade_no", AppUtil.getOrderNoAppendRandom(orderNO));
        params.put("cashier_trade_no", "");
        params.put("mcode", SpUtil.getBranchId() + "");
        params.put("device_en", SpUtil.getShopId());
        params.put("trade_status", "SUCCESS");
        params.put("time_create", time);
        params.put("time_end", time);
        params.put("total_fee", total_fee);
        params.put("pay_fee", total_fee);
        params.put("pay_type", pay_type);
        return params;
    }
    private void operateNext(){
        if ("shoppingViewModel".equals(intentFrom)){
            toNextPage();
        }else{
            vipChargeNext();
        }

    }
    private void toNextPage( ) {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        mRxManager.post(Constants.bus_type_http_result, Constants.type_order_pay_finish);
        Intent intent = new Intent(mActivity, ShoppingPayResultActivity.class);
        intent.putExtra(Constants.intent_orderNo, orderNO);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void vipChargeNext(){
        mActivity.hideRDialog();
        if (mRxManager == null) mRxManager = new RxManager();
        mRxManager.post(Constants.bus_type_http_result, Constants.type_charge_finish);
        Intent intent = new Intent(mActivity, PayResultActivity.class);
        intent.putExtra(Constants.intent_info, mChargeInfo);
        intent.putExtra(Constants.intent_orderNo, orderNO);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
    public void clear(){
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
    }
    public void onPause(){
        helper.onPause();
    }
}
