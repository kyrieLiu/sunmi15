package ys.app.pad.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.CommitOrderActivity;
import ys.app.pad.activity.PersonPayActivity;
import ys.app.pad.activity.SandQRCodeActivity;
import ys.app.pad.activity.ScannerGunPayCodeActivity;
import ys.app.pad.activity.ShoppingPayResultActivity;
import ys.app.pad.activity.vip.ChargeActivity;
import ys.app.pad.adapter.ShoppingCarAdapter;
import ys.app.pad.callback.KeyboardListener;
import ys.app.pad.databinding.ActivityCommitOrderBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.model.PayResultInfo;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.MathUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.timeselector.Utils.TextUtil;
import ys.app.pad.widget.wrapperRecylerView.DividerItemDecoration;

/**
 * Created by aaa on 2017/3/16.
 */

public class CommitOrderViewModel extends BaseActivityViewModel implements KeyboardListener{
    private final CommitOrderActivity mActivity;
    private final ActivityCommitOrderBinding mBinding;
    private final ApiClient<BaseListResult<SummitOrderInfo>> mClient;
    private ApiClient<BaseResult> payClient=new ApiClient<>();
    private ShoppingCarAdapter mServiceAdapter;
    private List<SummitOrderInfo> mServiceList = new ArrayList<>();
    private List<SummitOrderInfo> mGoodsList = new ArrayList<>();
    private ShoppingCarAdapter mGoodsAdapter;
    public ObservableBoolean isServiceVisible = new ObservableBoolean(true);
    public ObservableBoolean isGoodsVisible = new ObservableBoolean(true);
    public ObservableDouble obCountMoney = new ObservableDouble(0.00);
    public ObservableInt orderType = new ObservableInt(-1);//区分订单类型 0会员订单 1次卡订单 2普通订单
    private double countMoney = 0.00;
    private String mOrderNo;
    private CustomDialog mBackDialog;
    private List<SummitOrderInfo> orderData;

    private int isClassification;//区分次卡和会员  0是会员,1是次卡

    private String total_fee = "0";//支付金额，单位为分，1=0.01元，100=1元，不可空

    public ObservableBoolean obButtonEnable=new ObservableBoolean(false);
    public ObservableInt payType = new ObservableInt(-1);
    private RxManager mRxManager;
    private CustomDialog mDialog;
    private int mVipUserId;
    private MainConnectUtils utils;
    private StringBuilder shopMessage;

    private OrderPayParam sandPayParam;

    public CommitOrderViewModel(CommitOrderActivity activity, ActivityCommitOrderBinding binding,String userName,String userPhone) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
        utils = MainConnectUtils.getInstance().setContext(mActivity);
    }
    public void init() {
        mBinding.scrollView.setNestedScrollingEnabled(false);
        mServiceAdapter = new ShoppingCarAdapter(mActivity);
        mBinding.seviceRecylerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.seviceRecylerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division_line));
        mBinding.seviceRecylerView.setAdapter(mServiceAdapter);


        mGoodsAdapter = new ShoppingCarAdapter(mActivity);
        mBinding.goodsRecylerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.goodsRecylerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division_line));
        mBinding.goodsRecylerView.setAdapter(mGoodsAdapter);
        listenRealGet();

    }

    public void getDataFromIntent(String idStr) {
        this.mOrderNo = idStr;
        loadHttpData();
    }

    /**
     * 对实收金额监听
     */
    private void listenRealGet(){
        mBinding.keyboardLayout.setListener(this);
        mBinding.etCommitOrderRealGet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoft();
                closeKeybord(mBinding.etCommitOrderRealGet, mActivity);
                return false;
            }
        });
        mBinding.etCommitOrderRealGet.setFocusable(true);
        mBinding.etCommitOrderRealGet.setFocusableInTouchMode(true);
        mBinding.etCommitOrderRealGet.requestFocus();

        mBinding.etCommitOrderRealGet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (StringUtils.isEmptyOrWhitespace(text)) {
                    text = "0";
                }else{
                    text=text.replaceAll(",","");
                }
                double getMoney = Double.parseDouble(text);
                double returnMoney=subtract(getMoney,countMoney);
                mBinding.tvCommitOrderChange.setText(AppUtil.formatStandardMoney(returnMoney));
                total_fee = Math.round(getMoney * 100) + "";
            }
        });

        if (mRxManager==null)mRxManager=new RxManager();
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                //收款成功
                if (integer==Constants.type_order_pay_finish){
                    mActivity.finish();
                }
            }
        });
    }

    @Override
    public void reloadData(View view) {
        loadHttpData();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            isNetWorkErrorVisible.set(true);
        }
    }

    private void getDataList() {
        isLoadingVisible.set(true);
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("orderId", mOrderNo);
        params.put("limit","1000");

        mClient.reqApi("queryOrderList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<SummitOrderInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<SummitOrderInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            setData(result.getData());
                        } else {
                            isNetWorkErrorVisible.set(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                        super.onError(e);
                    }
                });
    }

    private void setData(List<SummitOrderInfo> data) {
        this.orderData=data;
        BigDecimal total=new BigDecimal(0);
        BigDecimal goodsTotal=new BigDecimal(0);
        BigDecimal serviceTotal=new BigDecimal(0);
            for (SummitOrderInfo info : data) {
                if (!(1 == info.getIsGift())) {
                    total = add(total, info.getRealMoney());
                }
                if (info.getType() == 2) {
                    mServiceList.add(info);
                    serviceTotal=add(serviceTotal,info.getRealMoney());
                } else if (info.getType() == 1) {
                    mGoodsList.add(info);
                    goodsTotal=add(goodsTotal,info.getRealMoney());
                }
            }
            countMoney=total.doubleValue();
            obCountMoney.set(countMoney);
            mBinding.etCommitOrderRealGet.setText(AppUtil.formatStandardMoney(countMoney));
            Editable text = mBinding.etCommitOrderRealGet.getText();
            mBinding.etCommitOrderRealGet.setSelection(text.length());
            Double orderMoney= MathUtil.retainTwoDecimal(countMoney);
            total_fee = Math.round(orderMoney * 100) + "";
            SummitOrderInfo summitOrderInfo=data.get(0);
             isClassification =summitOrderInfo.getIsClassification();

            if (isClassification == 0 || isClassification == 3) {//会员卡
                mVipUserId=summitOrderInfo.getVipUserId();
                orderType.set(0);
            } else if (isClassification == 1) {//次卡
                mVipUserId=summitOrderInfo.getVipUserId();
                orderType.set(1);
            }
        if (mServiceList.size() == 0) {
            isServiceVisible.set(false);
        } else {
            mServiceAdapter.setList(mServiceList);
        }
        if (mGoodsList.size() == 0) {
            isGoodsVisible.set(false);
        } else {
            mGoodsAdapter.setList(mGoodsList);
        }
        utils.showMenuMessage(data);
        setOrderToParam(data);

    }
    public BigDecimal add(BigDecimal b1, double v2) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * @param data
     */
    private void setOrderToParam(List<SummitOrderInfo> data){
        sandPayParam = new OrderPayParam();
        List<SummitOrderInfo> orderInfoList = data;

        sandPayParam.setOrderNo(orderInfoList.get(0).getOrderId());                            //目前取列表第一项的OrderId
        sandPayParam.setAmount(obCountMoney.get()+""); //付款金额
        sandPayParam.setGoodsName(SpUtil.getShopName());       //商品描述
        sendToT1miniStr();

    }

    public void back() {
        mBackDialog = new CustomDialog(mActivity);
        mBackDialog.setContent("确认放弃此订单？");
        mBackDialog.setCancelVisiable();
        mBackDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackDialog.dismiss();
                utils.showWelcomeMessage();
                mActivity.finish();
            }
        });
        if (!mActivity.isFinishing()) {
            mBackDialog.show();
        }
    }


    @Override
    public void onNumberClick(String num) {
        String content = mBinding.etCommitOrderRealGet.getText().append(num).toString();
        mBinding.etCommitOrderRealGet.setText(content);
        mBinding.etCommitOrderRealGet.setSelection(content.length());
    }

    @Override
    public void onPointClick() {
        String content = mBinding.etCommitOrderRealGet.getText().toString();
        boolean isContains = content.contains(".");
        if (!isContains) {
            mBinding.etCommitOrderRealGet.setText(mBinding.etCommitOrderRealGet.getText().append("."));
            mBinding.etCommitOrderRealGet.setSelection(content.length() + 1);
        }
    }

    @Override
    public void onDeleteClick() {
        Editable text = mBinding.etCommitOrderRealGet.getText();
        if (text.length() > 0) {
            mBinding.etCommitOrderRealGet.setText(text.delete(text.length() - 1, text.length()));
            mBinding.etCommitOrderRealGet.setSelection(text.length());
        }
    }

    @Override
    public void onOkClick() {
        String number = mBinding.etCommitOrderRealGet.getText().toString();
        double realGet=Double.parseDouble(number);
        double returnMoney=subtract(realGet,countMoney);
        mBinding.tvCommitOrderChange.setText(returnMoney+"");
        total_fee = Math.round(realGet * 100) + "";
    }

    private double subtract(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();

    }
    //微信支付
    public void clickWeixinPay(View v) {
        payType.set(0);
        sandPayParam.setPayChannelTypeNo("0205");
        startSandPay(sandPayParam);
    }
    //支付宝支付
    public void clickZhifubaoPay(View v) {
        payType.set(1);
        sandPayParam.setPayChannelTypeNo("0106");
        startSandPay(sandPayParam);
    }
    //微信扫描支付
    public void clickWeixinScannerPay(View v){
        payType.set(2);
        sandPayParam.setPayChannelTypeNo("0204");
        startSandPay(sandPayParam);
    }
    //支付宝扫描支付
    public void clickZhifubaoScannerPay(View v){
        payType.set(3);

        sandPayParam.setPayChannelTypeNo("0105");
        startSandPay(sandPayParam);
    }
    //银联卡支付
    public void clickXinhangkaPay(View v) {
        payType.set(4);
        bossPay("1006");
    }
    //现金支付
    public void clickXianjinPay(View v) {
        payType.set(5);
        bossPay("1001");
        AidlUtil.getInstance().openDrawer();
    }

    //个人微信支付
    public void clickBossWeixin(View view){
        payType.set(6);
        bossPay("0112");
    }

    //个人支付宝支付
    public void clickBossZhifubao(View view){
        payType.set(7);
        bossPay("0113");
    }
    //会员
    public void vipPay(View view){
        payType.set(8);
        payHttp();
        sendToT1miniStr();
    }
    //次卡
    public void numPay(View view){
        payType.set(9);
        payHttp();
    }

    /**
     * 杉德支付
     * @param sandPayParam   杉德支付参数
     */
    private void startSandPay(final OrderPayParam sandPayParam) {
        switch (sandPayParam.getPayChannelTypeNo()){
            case "0106":
            case "0205":
                Intent intent = new Intent(mActivity, SandQRCodeActivity.class);
                intent.putExtra(Constants.intent_info, sandPayParam);
                intent.putExtra(Constants.intent_name,"shoppingViewModel");
                mActivity.startActivityForResult(intent,Constants.request_code);
                break;
            case "0105":
            case "0204":
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
        }
    }

    public void toScanActivity(){
        Intent intent1 = new Intent(mActivity, ScannerGunPayCodeActivity.class);
        intent1.putExtra(Constants.intent_info, sandPayParam);
        intent1.putExtra(Constants.intent_name,"shoppingViewModel");
        mActivity.startActivityForResult(intent1,Constants.request_code );
    }

    //会员卡支付
    private void payHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", mOrderNo);
        params.put("shopId", SpUtil.getShopId() + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        mActivity.showRDialog();
        obButtonEnable.set(false);
        payClient.reqApi("vipPay", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        obButtonEnable.set(true);
                        if (result.isSuccess()) {
                            if (mRxManager==null)mRxManager=new RxManager();
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_order_pay_finish);
                            toNextPage( null);
                        } else {
                            if ("余额不足".equals(result.getData())) {
                                showDiaolog();//提示充值
                            } else {
                                toNextPage( null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        obButtonEnable.set(true);
                        toNextPage( null);
                    }
                });
    }



    private void toNextPage( PayResultInfo payResultInfo) {
        if (mRxManager==null) mRxManager=new RxManager();
        mRxManager.post(Constants.bus_type_http_result, Constants.type_order_pay_finish);
        Intent intent = new Intent(mActivity, ShoppingPayResultActivity.class);
        intent.putExtra(Constants.intent_orderNo, mOrderNo);
        intent.putExtra(Constants.intent_info, payResultInfo);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void showDiaolog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(mActivity);
            mDialog.setContent("会员卡余额不足，请充值后再支付");
            mDialog.setCancelable(false);
            mDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    Intent intent = new Intent(mActivity, ChargeActivity.class);
                    intent.putExtra(Constants.intent_id, (long) mVipUserId);//卡号
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
            });

            mDialog.setCancelVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    toNextPage( null);
                }
            });
        }
        mDialog.show();
    }

    /**
     *个人微信支付宝收款
     */
    private void bossPay(String pay_type){

        Intent intent=new Intent(mActivity, PersonPayActivity.class);
        intent.putExtra(Constants.intent_flag,pay_type);
        String getMoney=obCountMoney.get()+"";
        if (TextUtil.isEmpty(getMoney)){
            return;
        }
        intent.putExtra("money",getMoney);
        intent.putExtra(Constants.intent_name,"shoppingViewModel");
        intent.putExtra(Constants.intent_orderNo, mOrderNo);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SpUtil.getModelVice().equals("t1sub14")){
            utils.showMenuMessage(orderData);
        }else if (SpUtil.getModelVice().equals("t1sub7")){
            utils.showWelcomeMessage();
        }

    }

    //关闭键盘
    private void hideSoft() {
        mActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            mBinding.etCommitOrderRealGet.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mBinding.etCommitOrderRealGet, false);
            } catch (NoSuchMethodException e) {
                mBinding.etCommitOrderRealGet.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 发送订单金额给副屏
     */
    public void sendToT1miniStr(){
        if (Constants.T1mini.equals(Build.MODEL)) {
            Logger.d("发送副屏消息");
            AidlUtil.getInstance().setTextToT1mini(mActivity.getResources().getString(R.string.rmb) + sandPayParam.getAmount());
        }
    }
}
