package ys.app.pad.viewmodel;


import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.ServiceDetailActivity;
import ys.app.pad.adapter.SelectDiscountTypeAdapter;
import ys.app.pad.databinding.ServiceDetailActivityBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.DiscountInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;

/**
 * Created by Administrator on 2017/6/9.
 */

public class ServiceDetailViewModel extends BaseActivityViewModel {
    private final ServiceDetailActivity mActivity;
    private final ServiceDetailActivityBinding mBinding;
    private final ApiClient<BaseResult> mSummitClient;
    public ServiceInfo mResult;
    private ApiClient<BaseListResult<ServiceInfo>> mClient;
    private RxManager mRxManager;
    private int position;
    public ObservableBoolean promotion = new ObservableBoolean(false);

    /**
     * 折扣名字
     */
    public ObservableField<String> selectType = new ObservableField<>("折扣");
    /**
     * 一口价
     */
    public ObservableField<String> ykjString = new ObservableField<>();

    public ObservableField<String> discountString = new ObservableField<>();
    private List<DiscountInfo> mTypeData = new ArrayList<>();
    private SelectDialog mSelectDialog;
    public ObservableInt selectTypeId = new ObservableInt(discount_type_zhekou);
    public ObservableBoolean vipPic = new ObservableBoolean(false);
    public ObservableBoolean integralPic = new ObservableBoolean(false);
    private static final int discount_type_zhekou = 1;
    private static final int discount_type_lijian = 2;
    private int mZhekouDiscount;
    private String mZhekouPrice;
    private double mLijianDiscount;
    private String mLijianPrice;
    private RxManager rxManager;

    public ObservableField<String> obCostBeginDate = new ObservableField<>();//促销开始
    public ObservableField<String> obCostEndDate = new ObservableField<>();//促销截至


    public ServiceDetailViewModel(ServiceDetailActivity activity, ServiceDetailActivityBinding binding, ServiceInfo result, int position, boolean promotion) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
        this.position = position;
        this.mResult = result;
        this.promotion.set(promotion);
        this.mSummitClient = new ApiClient<>();
    }


    private void refreshData() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
//        params.put("typeId", mResult.getType() + "");
        params.put("id", String.valueOf(mResult.getId()));

        mClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            List<ServiceInfo> data = result.getData();
                            if (data != null && !data.isEmpty() && data.size() > 0) {
                                ServiceInfo serviceInfo = data.get(0);
                                mResult.setName(serviceInfo.getName());
                                mResult.setCostAmt(serviceInfo.getCostAmt());
                                mResult.setRealAmt(serviceInfo.getRealAmt());
                                mResult.setType(serviceInfo.getType());
                                mResult.setTypeName(serviceInfo.getTypeName());
                                mResult.setVipAmt(serviceInfo.getVipAmt());
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

    public void init() {
        if (mRxManager==null){
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.type_update_service, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                refreshData();
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
                    mZhekouPrice = AppUtil.formatStandardMoney((mResult.getRealAmt() * mZhekouDiscount) / (mZhekouDiscount < 10 ? 10 : 100));
                    mBinding.zhekouCuxiaojiaTv.setText("促销价" + mZhekouPrice + "元");
                } else {
                    mBinding.zhekouCuxiaojiaTv.setVisibility(View.GONE);
                }
            }
        });
    }

    public void costBeginDateTimeClick() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now=format.format(new Date());
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obCostBeginDate.set(time);
            }
        }, now, "2020-12-31 24:00");
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
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obCostEndDate.set(time);
            }
        }, startTime, "2025-12-31 24:00");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    /**
     * 折扣修改
     */
    public void clickButton() {

        if (TextUtils.isEmpty(obCostBeginDate.get())) {
            showToast(mActivity, "开始日期不能为空");
            return;
        }

        if (TextUtils.isEmpty(obCostEndDate.get())) {
            showToast(mActivity, "截至日期不能为空");
            return;
        }

//        Integer isFold:1代表折上折
//        promotionType:1:折扣 2:立减
//        promotionNum:折扣传0.6  立减 传20  100-20=80

        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("id", mResult.getId() + "");
        params.put("shopId", mResult.getShopId() + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("realAmt",mResult.getRealAmt()+"");
        params.put("isPromotion","1");

        if (discount_type_lijian == selectTypeId.get()) {//立减
            params.put("isFold", vipPic.get() ? "1" : "0");
            params.put("promotionType", "2");
            params.put("promotionNum", mLijianDiscount + "");
        } else {
            params.put("isFold", vipPic.get() ? "1" : "0");
            params.put("promotionType", "1");
            params.put("promotionNum", AppUtil.costParams(mZhekouDiscount));
        }

        String beginTime = obCostBeginDate.get();
        if (beginTime != null) {
            beginTime.replace("-", "");
            params.put("beginTime", beginTime);
        }

        String endTime = obCostEndDate.get();
        if (endTime != null) {
            endTime.replace("-", "");
            params.put("endTime", endTime);
        }

        mSummitClient.reqApi("updateService", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (rxManager == null) {
                                rxManager = new RxManager();
                            }
                            mActivity.showToast("修改成功");
                            rxManager.post(Constants.type_update_service, 1);
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
    public void selectClick() {
        if (mSelectDialog == null) {
            mTypeData.clear();
            DiscountInfo info = new DiscountInfo();
            info.setName("折扣");
            info.setType(discount_type_zhekou);

            DiscountInfo info1 = new DiscountInfo();
            info1.setName("立减");
            info1.setType(discount_type_lijian);

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
    public void vipClick() {
        if (vipPic.get()) {
            vipPic.set(false);
        } else {
            vipPic.set(true);
        }


    }

    /**
     * 积分
     */
    public void integralClick() {
        if (integralPic.get()) {
            integralPic.set(false);
        } else {
            integralPic.set(true);
        }
    }
    public void reset(){
        if (rxManager!=null){
            rxManager.clear();
            rxManager=null;
        }
    }

}
