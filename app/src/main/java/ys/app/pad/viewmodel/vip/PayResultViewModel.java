package ys.app.pad.viewmodel.vip;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Build;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.vip.PayActivity;
import ys.app.pad.activity.vip.PayResultActivity;
import ys.app.pad.databinding.ActivityPayResultBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.ChargeResultInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;

/**
 * Created by aaa on 2017/3/2.
 */

public class PayResultViewModel extends BaseActivityViewModel {
    private final PayResultActivity mActivity;
    private final ApiClient<BaseListResult<ChargeResultInfo>> mClient;
    public ObservableInt obSelectPayResultImg = new ObservableInt(R.mipmap.chongzhichenggong);
    public ObservableBoolean seePresenterMoney = new ObservableBoolean(false);
    public ObservableField<String> presenterMoney = new ObservableField<String>();


    public ObservableBoolean isOrderErrorVisible = new ObservableBoolean(false);
    public ObservableField<String> obSelectPayMoney = new ObservableField<>();
    public ObservableField<String> obSelectPayType = new ObservableField<>();
    public ObservableField<String> obSelectOrderNo = new ObservableField<>();
    private int countHttp;
    public ObservableBoolean obIsPaySuccess = new ObservableBoolean(false);
    private ChargeInfo mChargeInfo;
    private CustomDialog mBackDialog;
    private ChargeResultInfo mChargeResultInfo;
    private ChargeResultInfo.OrderDetailedListBean chargeDetail;
    private MainConnectUtils screenUtils;
    private String orderNO;

    public PayResultViewModel(PayResultActivity activity, ActivityPayResultBinding binding) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        screenUtils = MainConnectUtils.getInstance().setContext(mActivity);
    }

    public void setIntentData(ChargeInfo chargeInfo,String orderNO) {
        this.mChargeInfo = chargeInfo;
        this.orderNO=orderNO;
        loadDataHttp();
    }

    @Override
    public void reloadData(View view) {
        loadDataHttp();
    }

    private void loadDataHttp() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            countHttp++;
            getData();
        } else {//无网显示断网连接
            countHttp = 0;
            isNetWorkErrorVisible.set(true);
            mActivity.showToast(Constants.network_error_warn);
        }
    }

    private void getData() {
        isLoadingVisible.set(true);
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderNO);
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        mClient.reqApi("queryChargeOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ChargeResultInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ChargeResultInfo> result) {
                        isLoadingVisible.set(false);
                        List<ChargeResultInfo> data = result.getData();
                        if (data.size() != 0) {
                            ChargeResultInfo chargeResultInfo = data.get(0);
                            chargeDetail=chargeResultInfo.getRechargeList().get(0);
                            if (!"1".equals(chargeResultInfo.getStatus())) {
                                isOrderErrorVisible.set(true);
                                screenUtils.sendTextMessage("支付失败","");
                            } else {
                                setData(chargeResultInfo);
                            }
                        } else {
                            isOrderErrorVisible.set(true);
                            screenUtils.sendTextMessage("支付失败","");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoadingVisible.set(false);
                        onceMore();
                        super.onError(e);
                    }
                });

    }

    private void onceMore() {
        if (countHttp == 5) {
            isOrderErrorVisible.set(true);
            screenUtils.sendTextMessage("支付失败","");
        } else {
            loadDataHttp();
        }
    }

    private void setData(ChargeResultInfo resultInfo) {

        this.mChargeResultInfo = resultInfo;
        obIsPaySuccess.set(true);
        obSelectPayResultImg.set(R.mipmap.chongzhichenggong);
        obSelectPayMoney.set(AppUtil.formatStandardMoney(resultInfo.getRealAmt()));
        obSelectPayType.set(AppUtil.getPayType(resultInfo.getPayedMethod()));
        obSelectOrderNo.set(resultInfo.getOrderNo());
        presenterMoney.set(AppUtil.formatStandardMoney(chargeDetail.getPresentAmt()));
        StringBuilder resultBuilder=new StringBuilder();
        resultBuilder.append("支付完成\n充值会员: "+mChargeResultInfo.getVipName()+
                "\n充值金额: "+resultInfo.getRealAmt()+"元\n");
        if (chargeDetail.getPresentAmt()>0){
            resultBuilder.append("赠送金额: "+chargeDetail.getPresentAmt()+"元\n");
            seePresenterMoney.set(true);
        }
        resultBuilder.append("充值后余额"+mChargeResultInfo.getAfterAmt()+"元");
        screenUtils.sendTextMessage(resultBuilder.toString(),"");
        printPayInformation();
    }


    public void onClickButton(View view) {

        if (obIsPaySuccess.get()) {
            screenUtils.showWelcomeMessage();
            mActivity.finish();
        } else {//重新支付
            Intent intent = new Intent(mActivity, PayActivity.class);
            intent.putExtra(Constants.intent_info, mChargeInfo);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    public void onClickPrintReceipt(View view) {
        if (obIsPaySuccess.get()) {
            printPayInformation();
        } else {//取消
            showDialog();
        }

    }

    public void showDialog() {
        if (mBackDialog == null) {
            mBackDialog = new CustomDialog(mActivity);
            mBackDialog.setContent("确认放弃此订单？");
            mBackDialog.setCancelVisiable();
            mBackDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBackDialog.dismiss();
                    screenUtils.showWelcomeMessage();
                    mActivity.finish();
                }
            });
        }
        if (!mActivity.isFinishing()) {
            mBackDialog.show();
        }
    }

    public void onPause(){
        screenUtils.onPause();
    }

    /**
     * 打印支付信息
     * isSupply:是否是补打小票
     */
    private void printPayInformation() {
        if ("D1".equals(Build.MODEL)){
            BlueToothPrintUtil.getInstance().printChargePayInformation(mChargeResultInfo,chargeDetail);
        }else{
            AidlUtil.getInstance().printChargePayInformation(mChargeResultInfo,chargeDetail);
        }

    }
}
