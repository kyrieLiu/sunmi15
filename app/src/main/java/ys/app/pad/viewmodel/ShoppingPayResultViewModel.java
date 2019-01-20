package ys.app.pad.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.PayListActivity;
import ys.app.pad.activity.ShoppingPayResultActivity;
import ys.app.pad.databinding.ActivityShoppingPayResultBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;

/**
 * Created by aaa on 2017/3/16.
 */

public class ShoppingPayResultViewModel extends BaseActivityViewModel {

    private final ShoppingPayResultActivity mActivity;
    private final ApiClient<BaseListResult<OrderInfo>> mClient;
    private final ActivityShoppingPayResultBinding mBinding;
    public ObservableBoolean isOrderErrorVisible = new ObservableBoolean(false);
    public ObservableBoolean obIsPaySuccess = new ObservableBoolean(false);
    public ObservableField<String> obSelectPayType = new ObservableField<>();
    public ObservableField<String> obSelectOrderNo = new ObservableField<>();
    public ObservableField<String> obSelectPayMoney = new ObservableField<>();
    private int countHttp;
    private CustomDialog dialog;
    private String mOrderNo;
    private OrderInfo mOrderInfo;
    private MainConnectUtils screenUtils;

    public ShoppingPayResultViewModel(ShoppingPayResultActivity activity, ActivityShoppingPayResultBinding binding) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.mBinding = binding;
        screenUtils = MainConnectUtils.getInstance().setContext(mActivity);
    }

    public void setIntentData(String orderNo) {
        this.mOrderNo = orderNo;
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
        params.put("orderId", mOrderNo);
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        mClient.reqApi("queryOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<OrderInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<OrderInfo> result) {
                        isLoadingVisible.set(false);
                        List<OrderInfo> data = result.getData();
                        if (data.size() != 0) {
                            OrderInfo orderInfo = data.get(0);
                            if ("1".equals(orderInfo.getStatus())) {
                                setData(orderInfo);
                            } else {
                                isOrderErrorVisible.set(true);
                                screenUtils.sendTextMessage("支付失败","");
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

    private void setData(OrderInfo resultInfo) {
        this.mOrderInfo = resultInfo;

        obIsPaySuccess.set(true);
        obSelectPayMoney.set(resultInfo.getRealAmt() + "");
        obSelectPayType.set(AppUtil.getPayType(resultInfo.getPayedMethod()));
        obSelectOrderNo.set(resultInfo.getOrderNo());

        if (resultInfo.getIsClassification()==1){
            mBinding.rlShoppingPayMoney.setVisibility(View.GONE);
            screenUtils.sendTextMessage("支付完成","");
        }else{
            StringBuilder payBuilder=new StringBuilder();
            payBuilder.append("支付完成\n支付金额: "+resultInfo.getRealAmt()+"元");
            if (mOrderInfo.getVipUserId()>0){
                payBuilder.append("\n会员名称: "+resultInfo.getVipName()+"\n支付后余额"+resultInfo.getAfterAmt()+"元");
            }
            screenUtils.sendTextMessage(payBuilder.toString(),"");
        }

        printPayInformation();
    }

    public void clickLeftButton(View v) {
        if (obIsPaySuccess.get()) {//支付成功,补打小票
            printPayInformation();
        } else {//支付失败,取消支付
            showDialog();
        }

    }


    public void clickRightButton(View v) {
        screenUtils.showWelcomeMessage();
        if (obIsPaySuccess.get()) {//支付成功,确定
            mActivity.finish();
        } else {//支付失败,挂单
            insertPayListHttp();
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new CustomDialog(mActivity);
            dialog.setContent("确定放弃此订单?");
            dialog.setCancelVisiable();
            dialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    screenUtils.showWelcomeMessage();
                    mActivity.finish();
                }
            });
        }
        dialog.show();
    }

    /**
     * 加入待支付订单中
     */
    private void insertPayListHttp() {

        mActivity.turnToActivityByFinish(PayListActivity.class);

    }


    /**
     * 打印支付信息
     * isSupply:是否是补打小票
     */
    private void printPayInformation() {
        if (mOrderInfo.getIsClassification()==1){
            if ("D1".equals(Build.MODEL)) {
                BlueToothPrintUtil.getInstance().printNumOrderPayInformation(mOrderInfo);
            }else{
                AidlUtil.getInstance().printNumOrderPayInformation(mOrderInfo);
            }
        }else{
            if ("D1".equals(Build.MODEL)) {
                BlueToothPrintUtil.getInstance().printOrderPayInformation(mOrderInfo);
            } else{
                AidlUtil.getInstance().printOrderPayInformation(mOrderInfo,false);
            }
        }
    }
    public void onPause(){
        screenUtils.onPause();
    }
    public void onBackPressed(){
        screenUtils.showWelcomeMessage();
    }

}
