package ys.app.pad.viewmodel.vip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.PersonPayActivity;
import ys.app.pad.activity.ScannerGunPayCodeActivity;
import ys.app.pad.activity.SandQRCodeActivity;
import ys.app.pad.activity.vip.PayActivity;
import ys.app.pad.databinding.ActivityPayBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.utils.StringUtils;

/**
 * Created by aaa on 2017/3/1.
 */

public class PayViewModel extends BaseActivityViewModel {

    private final PayActivity mActivity;
    private final ApiClient<BaseResult> mClient;
    public ObservableInt payType = new ObservableInt(0);
    public ObservableBoolean obButtonEnable=new ObservableBoolean(false);
    public ChargeInfo mChargeInfo;
    private RxManager mRxManager;
    public ObservableBoolean seePresenter=new ObservableBoolean(false);

    private Map<String, String> params = new HashMap<>();
    private MainConnectUtils utils;
    private StringBuilder rechargeMessage=new StringBuilder();

    private OrderPayParam sandPayParam;

    public PayViewModel(PayActivity activity, ActivityPayBinding binding) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        utils = MainConnectUtils.getInstance().setContext(mActivity);
        mRxManager=new RxManager();
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer==Constants.type_charge_finish){
                    mActivity.finish();
                }
            }
        });

    }

    public void setIntentData(ChargeInfo chargeInfo) {
        this.mChargeInfo = chargeInfo;
        sandPayParam = new OrderPayParam();
        sandPayParam.setOrderNo(chargeInfo.getOrderNo());                            //目前取列表第一项的OrderId
        sandPayParam.setGoodsName(SpUtil.getShopName());       //商品描述
        sandPayParam.setPayChannelTypeNo("0205");
        if (mChargeInfo != null) {
                double charge = Double.parseDouble(mChargeInfo.getChargeMoney());
                double presenter=Double.parseDouble(mChargeInfo.getPresentAmt());
                sandPayParam.setAmount(charge+""); //付款金额
                rechargeMessage.append("充值会员: "+mChargeInfo.getName()+"\n"+"充值金额: "+charge+"元\n");
                if (presenter>0){
                    rechargeMessage.append("赠送金额: "+presenter+"元");
                    seePresenter.set(true);
                }
                obButtonEnable.set(true);
                utils.sendTextMessage(rechargeMessage.toString(),"");
            }

    }


    public void clickWeixinPay(View v) {
        sandPayParam.setPayChannelTypeNo("0205");
        payType.set(0);
    }

    public void clickZhifubaoPay(View v) {
        sandPayParam.setPayChannelTypeNo("0106");
        payType.set(1);
    }

    public void clickXinhangkaPay(View v) {
        payType.set(2);
    }

    public void clickXianjinPay(View v) {
        payType.set(3);
        AidlUtil.getInstance().openDrawer();
    }

    public void clickWeixinScannerPay(View v){
        sandPayParam.setPayChannelTypeNo("0204");
        payType.set(4);
    }
    public void clickZhifubaoScannerPay(View v){
        sandPayParam.setPayChannelTypeNo("0105");
        payType.set(5);
    }
    public void clickBossWeixin(View view){
        payType.set(6);
    }
    public void clickBossZhifubao(View view){
        payType.set(7);
    }


    /**
     * 結算
     *
     * @param v
     */
    public void clickButton(View v) {
        payHttp();
    }


    private void payHttp() {

        params.clear();
        params.put("shopId", SpUtil.getShopId() );
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("vipCardNo", mChargeInfo.getCardNo());
        params.put("orderNo", mChargeInfo.getOrderNo());
        if (StringUtils.isEmpty(mChargeInfo.getProductList())){
            params.put("isClassification","0");
        }else{
            params.put("isClassification","1");
        }
        mActivity.showRDialog();
        obButtonEnable.set(false);
        mClient.reqApi("insertRechargeOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            operaOrder();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        obButtonEnable.set(true);
                    }
                });
    }
    //处理支付逻辑
    private void operaOrder(){
        obButtonEnable.set(true);
        Intent intent=new Intent();
        switch (payType.get()){
            case 0:
            case 1:
                intent.setClass(mActivity,SandQRCodeActivity.class);
                intent.putExtra(Constants.intent_info, sandPayParam);
                intent.putExtra(Constants.intent_name,"payViewModel");
                mActivity.startActivityForResult(intent,Constants.request_code);
                break;
            case 2:
                break;
            case 3:
                bossPay("1001");
                break;
            case 4:
            case 5:
                if ("T1mini".equals(Build.MODEL)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, Constants.permission_request_code);
                        } else {
                            toScanActivity();
                        }
                    } else {
                        toScanActivity();
                    }
                }else {
                    toScanActivity();
                }
                break;
            case 6:
                bossPay("0112");
                break;
            case 7:
                bossPay("0113");
                break;

        }

    }

    public void  toScanActivity(){
        Intent intent=new Intent();
        intent.setClass(mActivity, ScannerGunPayCodeActivity.class);
        intent.putExtra(Constants.intent_info, sandPayParam);
        intent.putExtra(Constants.intent_name,"payViewModel");
        mActivity.startActivityForResult(intent,Constants.request_code );
    }

    private void bossPay(String pay_type){

        Intent intent=new Intent(mActivity, PersonPayActivity.class);
        intent.putExtra(Constants.intent_flag,pay_type);
        intent.putExtra("money",mChargeInfo.getChargeMoney());
        intent.putExtra("chargeModel",mChargeInfo);
        intent.putExtra(Constants.intent_name,"payViewModel");
        intent.putExtra(Constants.intent_orderNo,sandPayParam.getOrderNo())        ;
        mActivity.startActivityForResult(intent,Constants.request_code);
    }


    public void clear(){
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
    }
    public void onPause(){
        utils.onPause();
    }
    public void onBackPress(){
        utils.showWelcomeMessage();
        mActivity.finish();
    }

    public void onActivityResult() {
        //utils = MainConnectUtils.getInstance().setContext(mActivity);
        utils.sendTextMessage(rechargeMessage.toString(),"");
    }
}
