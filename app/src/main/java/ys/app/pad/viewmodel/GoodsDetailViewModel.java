package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.AddInventoryActivity;
import ys.app.pad.activity.GoodsDetailActivity;
import ys.app.pad.activity.ModifyActivity;
import ys.app.pad.activity.TakeOutInventoryActivity;
import ys.app.pad.adapter.SelectDiscountTypeAdapter;
import ys.app.pad.databinding.ActivityGoodsDetailBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.DiscountInfo;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;

/**
 * Created by aaa on 2017/3/7.
 */

public class GoodsDetailViewModel extends BaseActivityViewModel {

    private final ApiClient<BaseResult> mSummitClient;

    private final GoodsDetailActivity mActivity;
    private final ActivityGoodsDetailBinding mBinding;
    private final TextView mModifyTv;
    //    private  String mFlag;
    public GoodInfo mResult;
    private RxManager mRxManager;
    private ApiClient<BaseListResult<GoodInfo>> mClient;
    public ObservableBoolean isInventory = new ObservableBoolean(false);
    public ObservableBoolean promotion = new ObservableBoolean(false);
    /**
     * 折扣名字
     */
    public ObservableField<String> selectType = new ObservableField<>("折扣");
    /**
     * 折扣
     */
    public ObservableField<String> ykjString = new ObservableField<>();

    public ObservableField<String> discountString = new ObservableField<>();
    private List<DiscountInfo> mTypeData = new ArrayList<>();
    private SelectDialog mSelectDialog;
    public ObservableInt selectTypeId = new ObservableInt(discount_type_zhekou);
    public ObservableBoolean vipPic = new ObservableBoolean(true);
    public ObservableBoolean integralPic = new ObservableBoolean(false);
    private static final int discount_type_zhekou = 1;
    private static final int discount_type_lijian = 2;
    private int mZhekouDiscount;
    private String mZhekouPrice;
    private double mLijianDiscount;
    private String mLijianPrice;
    private RxManager rxManager;
    public ObservableField<String> obBarCode = new ObservableField<>();
    public ObservableField<String> obCostAmt = new ObservableField<>();
    public ObservableDouble obSalesAmt = new ObservableDouble();
    public ObservableField<String> obCostBeginDate = new ObservableField<>();//促销开始
    public ObservableField<String> obCostEndDate = new ObservableField<>();//促销截至
    private DeleteDialog mConfirmDeleteDialog;



    public GoodsDetailViewModel(GoodsDetailActivity activity, ActivityGoodsDetailBinding binding, GoodInfo result, boolean promotion, TextView mModifyTv) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mRxManager = new RxManager();
        this.mClient = new ApiClient<>();
        this.mResult = result;
        obCostAmt.set("查看");

        obBarCode.set(mResult.getBarCode().equals(Constants.barCodeTemp_int)?"无":mResult.getBarCode());
        obSalesAmt.set(mResult.getRealAmt());
//        this.mFlag = flag;
        this.promotion.set(promotion);
        this.mSummitClient = new ApiClient<>();
        this.mModifyTv = mModifyTv;
        init();

    }

    private void init() {

        mModifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showModifyDialog();
            }
        });


        mBinding.lijianEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) return;
                String s = editable.toString().replaceAll(",","");
                if (!StringUtils.isEmptyOrWhitespace(s)) {

                    mLijianDiscount = Double.parseDouble(s);
                    mLijianPrice = AppUtil.formatStandardMoney(mResult.getRealAmt() - Double.parseDouble(s));
                    mBinding.lijianCuxiaojiaTv.setVisibility(View.VISIBLE);
                    mBinding.lijianCuxiaojiaTv.setText("促销价" + mLijianPrice + "元");

                } else {
                    mBinding.lijianCuxiaojiaTv.setVisibility(View.GONE);
                }
            }
        });


        mBinding.zhekouEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) return;
                String s = editable.toString();
                if (!StringUtils.isEmptyOrWhitespace(s)) {

                    mBinding.zhekouCuxiaojiaTv.setVisibility(View.VISIBLE);

                    mZhekouDiscount = Integer.parseInt(s);
                    mZhekouPrice = AppUtil.formatStandardMoney((mResult.getRealAmt() * mZhekouDiscount) / (mZhekouDiscount<10?10:100));
                    mBinding.zhekouCuxiaojiaTv.setText("促销价" + mZhekouPrice + "元");
                } else {
                    mBinding.zhekouCuxiaojiaTv.setVisibility(View.GONE);
                }
            }
        });
    }
    private void showModifyDialog() {

        if (mConfirmDeleteDialog == null){
            mConfirmDeleteDialog = new DeleteDialog(mActivity);
        }
        mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
            @Override
            public void verificationPwd(boolean b) {
                if (b){
                    if (mConfirmDeleteDialog != null){
                        mConfirmDeleteDialog.dismiss();
                    }
                    Intent intent = new Intent(mActivity,ModifyActivity.class);
                    intent.putExtra(Constants.intent_info,mResult);
                    mActivity.startActivityForResult(intent,10);
                }else {
                    Toast.makeText(mActivity,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDismiss() {
                mConfirmDeleteDialog = null;
            }
        });
        mConfirmDeleteDialog.show();
    }



    public void costBeginDateTimeClick() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now=format.format(new Date());
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obCostBeginDate.set(time);
            }
        }, now, "2025-12-31 24:00");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    public void costEndDateTimeClick() {
        String startTime=obCostBeginDate.get();
        if (StringUtils.isEmptyOrWhitespace(startTime)){
            Toast.makeText(mActivity,"请选择开始日期",Toast.LENGTH_SHORT).show();
            return;
        }
        startTime =startTime+" 00:00";
        Log.d("TAG", "costEndDateTimeClick: "+startTime);
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obCostEndDate.set(time);
            }
        }, startTime, "2025-12-31 24:00");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }


    private void refreshData() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId()+"");
        params.put("id", mResult.getId() + "");

        mClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            List<GoodInfo> data = result.getData();
                            if (data != null && !data.isEmpty()) {
                                GoodInfo goodInfo = data.get(0);
                                mResult.setName(goodInfo.getName());
                                mResult.setStockNum(goodInfo.getStockNum());
                                mResult.setInventoryNum(goodInfo.getInventoryNum());
                                mResult.setBarCode(goodInfo.getBarCode());
                                mResult.setCostAmt(goodInfo.getCostAmt());
                                obCostAmt.set("查看");
                                obBarCode.set(goodInfo.getBarCode().equals(Constants.barCodeTemp_int)?"无":goodInfo.getBarCode());
                                obSalesAmt.set(goodInfo.getRealAmt());
                                mResult.setType(goodInfo.getType());
                                mResult.setTypeName(goodInfo.getTypeName());
                                mResult.setUnit(goodInfo.getUnit());
                                mResult.setVipAmt(goodInfo.getVipAmt());
                            } else {
                                mActivity.showToast("查询无结果");
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });

    }

    //点击了入库
    public void clickInnerInventory(View v) {
        Intent intent = new Intent(mActivity, AddInventoryActivity.class);
        intent.putExtra(Constants.intent_info, mResult);
        mActivity.startActivity(intent);
    }

    //点击了出库
    public void clickOutterInventory(View v) {
        Intent intent = new Intent(mActivity, TakeOutInventoryActivity.class);
        intent.putExtra(Constants.intent_info, mResult);
        mActivity.startActivity(intent);
    }

    /**
     * 折扣修改
     */
    public void clickButton(){

        if(TextUtils.isEmpty(obCostBeginDate.get())){
            showToast(mActivity,"请选择开始时间");
            return;
        }

        if(TextUtils.isEmpty(obCostEndDate.get())){
            showToast(mActivity,"请选择结束时间");
            return;
        }

        mActivity.showRDialog();
        Map<String,String> params = new HashMap<>();
        params.put("id", mResult.getId() + "");
        params.put("shopId", mResult.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("realAmt",mResult.getRealAmt()+"");
        params.put("isPromotion","1");


        if (discount_type_lijian == selectTypeId.get()) {//立减
            params.put("isFold",vipPic.get()?"1":"0");
            params.put("promotionType","2");
            params.put("promotionNum",mLijianDiscount+"");
        }else {
            params.put("isFold",vipPic.get()?"1":"0");
            params.put("promotionType","1");
            params.put("promotionNum",AppUtil.costParams(mZhekouDiscount));
        }
        String beginTime = obCostBeginDate.get();
        String endTime = obCostEndDate.get();
        beginTime.replace("-", "");
        params.put("beginTime",beginTime);
        endTime.replace("-", "");
        params.put("endTime",endTime);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String isPromotionType="1";
        try {
            Date startDate=format.parse(beginTime);
            Date endDate=format.parse(endTime);
            Date date=new Date();
            if (startDate.getTime()<=date.getTime()&&endDate.getTime()>=date.getTime()){
                isPromotionType="1";
            }else{
                isPromotionType="0";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            isPromotionType="1";
        }
        params.put("isPromotion",isPromotionType);

        mSummitClient.reqApi("updateGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (rxManager == null) {
                                rxManager = new RxManager();
                            }
                            mActivity.showToast("修改成功");
                            rxManager.post(Constants.bus_type_http_result, Constants.type_updateGoods_success);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });
    }

    /**
     * 折扣类型选择
     */
    public void selectClick(){
        if (mSelectDialog == null) {
            mTypeData.clear();
            DiscountInfo info = new DiscountInfo();
            info.setName("折扣");
            info.setType(1);

            DiscountInfo info1 = new DiscountInfo();
            info1.setName("立减");
            info1.setType(2);

            mTypeData.add(info);
            mTypeData.add(info1);
            SelectDiscountTypeAdapter adapter = new SelectDiscountTypeAdapter(mActivity, mTypeData);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    DiscountInfo info = mTypeData.get(position);
                    selectType.set(info.getName());
                    selectTypeId.set(info.getType());
                }
            });
        } else {
            mSelectDialog.setDiscountData(mTypeData);
        }
        mSelectDialog.show();

    }

    /**
     * vip折上折
     */
    public void vipClick(){
        if (vipPic.get()){
            vipPic.set(false);
        }else {
            vipPic.set(true);
        }
    }

    /**
     * 查看成本价需要店长密码
     */
    public void verPassword(){
        if (mConfirmDeleteDialog == null){
            mConfirmDeleteDialog = new DeleteDialog(mActivity);
        }
        mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
            @Override
            public void verificationPwd(boolean b) {
                if (b){
                    if (mConfirmDeleteDialog != null){
                        mConfirmDeleteDialog.dismiss();
                    }
                    obCostAmt.set(AppUtil.formatStandardMoney(mResult.getCostAmt())+"元");
                }else {
                    Toast.makeText(mActivity,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDismiss() {
                mConfirmDeleteDialog = null;
            }
        });
        mConfirmDeleteDialog.show();
    }

    public void onAcitivytResult(int requestCode, int resultCode, Intent data){
        if (requestCode==10){
            if (resultCode==10){
                refreshData();
            }
        }
    }

    /**
     * 积分
     */
    public void integralClick(){
        if (integralPic.get()){
            integralPic.set(false);
        }else {
            integralPic.set(true);
        }
    }
    public void clear(){
        mConfirmDeleteDialog=null;
        mSelectDialog=null;
        if (rxManager!=null){
            rxManager.clear();
            rxManager=null;
        }
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
    }

}
