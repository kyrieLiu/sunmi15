package ys.app.pad.viewmodel.manage;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BR;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.AddVipCardActivity;
import ys.app.pad.databinding.ActivityAddVipCardBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by aaa on 2017/3/17.
 *
 * @modify XYY 2017/6/18
 * 折扣：
 * 商品和服务的折扣为0时不提交
 * 提交接口时一位数,除以10
 * 两位数除以100
 */

public class AddVipCardViewModel extends BaseActivityViewModel {

    private final AddVipCardActivity mActivity;
    private final ActivityAddVipCardBinding mBinding;
    private final ApiClient<BaseResult> mClient;
    public ObservableField<String> obCardName = new ObservableField<>();
    public ObservableField<String> obGoodCost = new ObservableField<>();
    public ObservableField<String> obServiceCost = new ObservableField<>();
    private RxManager mRxManager;
    @Bindable
    private VipCardInfo mInfo;
    public ObservableBoolean obButtonEnable = new ObservableBoolean(true);
    public List<GoodTypeInfo> goodTypeInfos = new ArrayList<>();
    public List<ServiceTypeInfo> serviceTypeInfos = new ArrayList<>();
    public ObservableBoolean obIsInput = new ObservableBoolean(true);
    public ObservableBoolean obIsIsAddVipCard = new ObservableBoolean(true);
    private String cardName;
    private int classification;

    public AddVipCardViewModel(AddVipCardActivity activity, ActivityAddVipCardBinding binding,int classification) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<BaseResult>();
        this.classification=classification;
    }

    public void setIntentData(VipCardInfo info) {
        obIsIsAddVipCard.set(false);
        setmInfo(info);
        obCardName.set(mInfo.getName());
        cardName=mInfo.getName();
        if (!TextUtils.isEmpty(mInfo.getCommodityDiscount() + "")) {
            obGoodCost.set(AppUtil.discountShow(mInfo.getCommodityDiscount()));
        }

        if (!TextUtils.isEmpty(mInfo.getProductDiscount() + "")) {
            obServiceCost.set(AppUtil.discountShow(mInfo.getProductDiscount()));
        }

    }

    public void init() {
        if (mInfo == null) {
            goodTypeInfos.addAll(GreenDaoUtils.getmDaoSession().getGoodTypeInfoDao().loadAll());
            serviceTypeInfos.addAll(GreenDaoUtils.getmDaoSession().getServiceTypeInfoDao().loadAll());
        }else {
            goodTypeInfos.addAll(mInfo.getCardDiscountList());
            serviceTypeInfos.addAll(mInfo.getCardDiscountList2());
        }
        mBinding.scrollView.setFocusableInTouchMode(true);
        mBinding.scrollView.requestFocus();
        mBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                mBinding.scrollView.smoothScrollTo(0,0);
            }
        });

        SpannableString msp = new SpannableString("例：9折即在原价上*0.9,95折即在原价上*0.95,不打折即无折扣。");
        msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.blue)), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
        msp.setSpan(new UnderlineSpan(), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.blue)), 14, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
        msp.setSpan(new UnderlineSpan(), 14, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.blue)), 28, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为洋红色
        msp.setSpan(new UnderlineSpan(), 28, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvVipCardWarn.setText(msp);

    }

    public void clickButton(View v) {
        if (TextUtil.isEmpty(obCardName.get())){
            Toast.makeText(mActivity,"请输入会员卡名称",Toast.LENGTH_SHORT).show();
            return;
        }
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("classification",classification+"");
        if (goodTypeInfos != null && !goodTypeInfos.isEmpty()) {
            params.put("commodityDiscount", new Gson().toJson(goodTypeInfos));
        }

        if (serviceTypeInfos != null && !serviceTypeInfos.isEmpty()) {
            params.put("productDiscount", new Gson().toJson(serviceTypeInfos));
        }

        obButtonEnable.set(false);
        if (mInfo == null) {//新增
            params.put("name", obCardName.get());
            mClient.reqApi("addVipCard", params, ApiRequest.RespDataType.RESPONSE_JSON)
                    .updateUI(new Callback<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult info) {
                            mActivity.hideRDialog();
                            if (info.isSuccess()) {
                                if (mRxManager == null) {
                                    mRxManager = new RxManager();
                                }
                                mRxManager.post(Constants.bus_type_http_result, Constants.type_addVipCard_success);
                                mActivity.finish();
                            } else {
                                obButtonEnable.set(true);
                                mActivity.showToast(info.getErrorMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mActivity.hideRDialog();
                            super.onError(e);
                            obButtonEnable.set(true);
                        }
                    });
        } else {//编辑
            if (!cardName.equals(obCardName.get())){//如果改会员卡名称需要更新名称
                params.put("name", obCardName.get());
            }
            params.put("id", mInfo.getId() + "");
            if (goodTypeInfos != null && !goodTypeInfos.isEmpty()) {
                params.put("commodityDiscount", new Gson().toJson(goodTypeInfos));
            }

            if (serviceTypeInfos != null && !serviceTypeInfos.isEmpty()) {
                params.put("productDiscount", new Gson().toJson(serviceTypeInfos));
            }

            mClient.reqApi("updateVipCard", params, ApiRequest.RespDataType.RESPONSE_JSON)
                    .updateUI(new Callback<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult info) {
                            mActivity.hideRDialog();
                            if (info.isSuccess()) {
                                if (mRxManager == null) {
                                    mRxManager = new RxManager();
                                }
                                mRxManager.post(Constants.bus_type_http_result, Constants.type_updateVipCard_success);
                                mActivity.finish();
                            } else {
                                obButtonEnable.set(true);
                                mActivity.showToast(info.getErrorMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mActivity.hideRDialog();
                            super.onError(e);
                            mActivity.showToast(e.toString());
                            obButtonEnable.set(true);
                        }
                    });
        }
    }


    @Bindable
    public VipCardInfo getmInfo() {
        return mInfo;
    }

    public void setmInfo(VipCardInfo mInfo) {
        this.mInfo = mInfo;
        notifyPropertyChanged(BR.mInfo);
    }
}
